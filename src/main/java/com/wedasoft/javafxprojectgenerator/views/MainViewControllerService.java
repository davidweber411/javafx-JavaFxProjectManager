package com.wedasoft.javafxprojectgenerator.views;

import com.wedasoft.javafxprojectgenerator.MainApplicationLauncher;
import com.wedasoft.javafxprojectgenerator.enums.ModuleSystemType;
import com.wedasoft.javafxprojectgenerator.exceptions.NotValidException;
import com.wedasoft.javafxprojectgenerator.helper.HelperFunctions;
import com.wedasoft.javafxprojectgenerator.services.FileModificationService;
import com.wedasoft.javafxprojectgenerator.services.ZipService;
import com.wedasoft.simpleJavaFxApplicationBase.jfxDialogs.JfxDialogUtil;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.stage.DirectoryChooser;
import javafx.stage.Window;
import lombok.Getter;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static com.wedasoft.javafxprojectgenerator.helper.PathConstants.*;
import static java.nio.file.Path.of;

@SuppressWarnings("ClassCanBeRecord")
@Getter
public class MainViewControllerService {

    private final MainViewController viewController;

    public MainViewControllerService(MainViewController viewController) {
        this.viewController = viewController;
    }

    public void onResetButtonClick(@SuppressWarnings("unused") ActionEvent event) {
        viewController.getApplicationNameTextField().setText("");
        viewController.getGroupIdTextField().setText("");
        viewController.getModuleSystemTypeChoiceBox().setValue(null);
        viewController.getDestinationDirectoryTextField().setText("");
    }

    public void onChooseDestinationDirectoryButtonClick(@SuppressWarnings("unused") ActionEvent event) {
        DirectoryChooser dc = new DirectoryChooser();
        dc.setTitle("Choose the destination directory");
        dc.setInitialDirectory(of(System.getProperty("user.home")).toFile());
        Window ownerWindow = ((Node) event.getSource()).getScene().getWindow();
        File file = dc.showDialog(ownerWindow);
        if (file != null) {
            viewController.getDestinationDirectoryTextField().setText(file.getAbsolutePath());
        }
    }

    public void onMenuItemCloseClick() {
        JfxDialogUtil.displayExitProgramDialog();
    }

    public void onCreateProjectButtonClick(@SuppressWarnings("unused") ActionEvent event) {
        try {
            validateForm();
            createProject();
        } catch (NotValidException nve) {
            JfxDialogUtil.createErrorDialog(nve.getMessage()).showAndWait();
        } catch (Exception e) {
            JfxDialogUtil.createErrorDialog("Could not create the project.", e);
            e.printStackTrace();
        }
    }

    private void validateForm() throws NotValidException {
        if (viewController.getApplicationNameTextField().getText().isBlank()) {
            throw new NotValidException("You must enter an application name.");
        }
        if (!viewController.getApplicationNameTextField().getText().matches("[a-zA-Z0-9]+")) {
            throw new NotValidException("The application name must only contain the characters a-z, A-Z, 0-9.");
        }

        if (viewController.getGroupIdTextField().getText().isBlank()) {
            throw new NotValidException("You must enter group id.");
        }
        if (!viewController.getGroupIdTextField().getText().matches("[a-zA-Z0-9.]+")) {
            throw new NotValidException("The group id must only contain the characters a-z, A-Z, 0-9, (dot).");
        }
        if (viewController.getGroupIdTextField().getText().charAt(0) == '.') {
            throw new NotValidException("The first character of the group id mustn't be a dot.");
        }
        if (viewController.getGroupIdTextField().getText().charAt(viewController.getGroupIdTextField().getText().length() - 1) == '.') {
            throw new NotValidException("The last character of the group id mustn't be a dot.");
        }
        if (Arrays.stream(viewController.getGroupIdTextField().getText().split("\\."))
                .anyMatch(part -> HelperFunctions.isNumeric(part.charAt(0) + ""))) {
            throw new NotValidException("Group id parts mustn't begin with a number.");
        }

        if (viewController.getVersionTextField().getText().isBlank()) {
            throw new NotValidException("You must enter a version.");
        }
        if (viewController.getModuleSystemTypeChoiceBox().getValue() == null) {
            throw new NotValidException("You must select a module system type.");
        }
        if (viewController.getDestinationDirectoryTextField().getText().isBlank()) {
            throw new NotValidException("You must specify a target destination path.");
        }
        if (Files.isRegularFile(Paths.get(viewController.getDestinationDirectoryTextField().getText()))) {
            throw new NotValidException("The specified target directory must not be a file.");
        }
        if (Files.exists(getNewProjectDestinationDirPath())) {
            throw new NotValidException("There already exists an project with the name '"
                    + viewController.getApplicationNameTextField().getText() + "' in the target directory.");
        }
    }

    private Path getNewProjectDestinationDirPath() {
        return Paths.get(viewController.getDestinationDirectoryTextField().getText())
                .resolve(of(viewController.getApplicationNameTextField().getText()));
    }

    private void createProject() throws Exception {
        createAndCleanTmpDirectoryAndUnzipTmpProject();

        modifySettingsGradle();
        modifyBuildGradle();
        modifyMainApplicationLauncherJava();
        modifyMainApplicationJava();
        modifyMainViewControllerJava();
        modifyMainViewFxml();

        moveSrcMainJavaContentFromZipToProjectPackages();
        moveSrcMainResourcesContentFromZipToProjectPackages();

        FileUtils.moveDirectory(
                userHomeApplicationPath.resolve(of("JavaFxAppNonModular")).toFile(),
                getNewProjectDestinationDirPath().toFile());

        JfxDialogUtil.createInformationDialog("Project created successfully.").showAndWait();
    }

    private void createAndCleanTmpDirectoryAndUnzipTmpProject() throws Exception {
        Files.createDirectories(userHomeApplicationPath);
        FileUtils.cleanDirectory(userHomeApplicationPath.toFile());

        ZipService.getInstance().extractZipFileFromClassPath(
                MainApplicationLauncher.class,
                viewController.getModuleSystemTypeChoiceBox().getValue().getClassPathOfZip(),
                userHomeApplicationPath);
    }

    private void moveSrcMainResourcesContentFromZipToProjectPackages() throws IOException {
        Path resourcesDirPath = getPathTo(srcMainResources);

        List<String> groupIdParts = Arrays.stream(viewController.getGroupIdTextField().getText().split("\\.")).toList();
        for (String part : groupIdParts) {
            resourcesDirPath = resourcesDirPath.resolve(part);
        }
        resourcesDirPath = resourcesDirPath.resolve(viewController.getApplicationNameTextField().getText().toLowerCase());

        FileUtils.moveDirectory(getPathTo(srcMainResourcesYourGroupIdJavafxappnonmodular).toFile(), resourcesDirPath.toFile());
        FileUtils.deleteDirectory(getPathTo(srcMainResourcesYour).toFile());
    }

    private void moveSrcMainJavaContentFromZipToProjectPackages() throws IOException {
        Path projectSrcArtifactPath = getPathTo(srcMainJava);

        List<String> groupIdParts = Arrays.stream(viewController.getGroupIdTextField().getText().split("\\.")).toList();
        for (String part : groupIdParts) {
            projectSrcArtifactPath = projectSrcArtifactPath.resolve(part);
        }
        projectSrcArtifactPath = projectSrcArtifactPath.resolve(viewController.getApplicationNameTextField().getText().toLowerCase());

        FileUtils.moveDirectory(getPathTo(srcMainJavaYourGroupIdJavafxappnonmodular).toFile(), projectSrcArtifactPath.toFile());
        FileUtils.deleteDirectory(getPathTo(srcMainJavaYour).toFile());
    }

    private void modifyMainViewFxml() throws IOException {
        if (viewController.getModuleSystemTypeChoiceBox().getValue() == ModuleSystemType.NON_MODULAR) {
            FileModificationService.getInstance().modifyAndWriteFile(
                    getPathTo(srcMainResourcesYourGroupIdJavafxappnonmodularViewsMainViewFxml),
                    Map.ofEntries(
                            Map.entry("your.groupId.javafxappnonmodular.views.MainViewController",
                                    String.format("%s.%s.views.MainViewController",
                                            viewController.getGroupIdTextField().getText(), viewController.getApplicationNameTextField().getText().toLowerCase()))));
        }
    }

    private void modifyMainViewControllerJava() throws IOException {
        if (viewController.getModuleSystemTypeChoiceBox().getValue() == ModuleSystemType.NON_MODULAR) {
            FileModificationService.getInstance().modifyAndWriteFile(
                    getPathTo(srcMainJavaYourGroupIdJavafxappnonmodularViewsMainViewControllerJava),
                    Map.ofEntries(
                            Map.entry("package your.groupId.javafxappnonmodular.views;",
                                    String.format("package %s.%s.views;",
                                            viewController.getGroupIdTextField().getText(), viewController.getApplicationNameTextField().getText().toLowerCase()))));
        }
    }

    private void modifyMainApplicationJava() throws IOException {
        if (viewController.getModuleSystemTypeChoiceBox().getValue() == ModuleSystemType.NON_MODULAR) {
            FileModificationService.getInstance().modifyAndWriteFile(
                    getPathTo(srcMainJavaYourGroupIdJavafxappnonmodularMainApplicationJava),
                    Map.ofEntries(
                            Map.entry("package your.groupId.javafxappnonmodular;",
                                    String.format("package %s.%s;",
                                            viewController.getGroupIdTextField().getText(), viewController.getApplicationNameTextField().getText().toLowerCase()))));
        }
    }

    private void modifyMainApplicationLauncherJava() throws IOException {
        if (viewController.getModuleSystemTypeChoiceBox().getValue() == ModuleSystemType.NON_MODULAR) {
            FileModificationService.getInstance().modifyAndWriteFile(
                    getPathTo(srcMainJavaYourGroupIdJavafxappnonmodularMainApplicationLauncherJava),
                    Map.ofEntries(
                            Map.entry("package your.groupId.javafxappnonmodular;",
                                    String.format("package %s.%s;",
                                            viewController.getGroupIdTextField().getText(), viewController.getApplicationNameTextField().getText().toLowerCase()))));
        }
    }

    private void modifyBuildGradle() throws IOException {
        if (viewController.getModuleSystemTypeChoiceBox().getValue() == ModuleSystemType.NON_MODULAR) {
            FileModificationService.getInstance().modifyAndWriteFile(
                    getPathTo(buildGradle),
                    Map.ofEntries(
                            Map.entry("group 'com.wedasoft'",
                                    String.format("group '%s'",
                                            viewController.getGroupIdTextField().getText())),
                            Map.entry("version '2.0.0'",
                                    String.format("version '%s'",
                                            viewController.getVersionTextField().getText())),
                            Map.entry("mainClassNameParam = 'your.groupId.javafxappnonmodular.MainApplicationLauncher'",
                                    String.format("mainClassNameParam = '%s.%s.MainApplicationLauncher'",
                                            viewController.getGroupIdTextField().getText(), viewController.getApplicationNameTextField().getText().toLowerCase()))));
        }
    }

    private void modifySettingsGradle() throws IOException {
        if (viewController.getModuleSystemTypeChoiceBox().getValue() == ModuleSystemType.NON_MODULAR) {
            FileModificationService.getInstance().modifyAndWriteFile(
                    getPathTo(settingsGradle),
                    Map.ofEntries(
                            Map.entry("rootProject.name = \"JavaFxAppNonModular\" //willBeInitilizedByJavaFxProjectGenerator",
                                    "rootProject.name = \"" + viewController.getApplicationNameTextField().getText() + "\"")));
        }
    }

    private Path getPathTo(String[] dirPathPartsToMainViewFxml) {
        String[] zipFilePathParts = viewController.getModuleSystemTypeChoiceBox().getValue().getClassPathOfZip().split("/");
        String tmpProjectName = zipFilePathParts[zipFilePathParts.length - 1];
        String tmpZipProjectName = tmpProjectName.substring(0, tmpProjectName.lastIndexOf('.'));

        return userHomeApplicationPath.resolve(of(tmpZipProjectName, dirPathPartsToMainViewFxml));
    }

}
