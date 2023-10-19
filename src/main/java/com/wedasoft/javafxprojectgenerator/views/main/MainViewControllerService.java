package com.wedasoft.javafxprojectgenerator.views.main;

import com.wedasoft.javafxprojectgenerator.MainApplicationLauncher;
import com.wedasoft.javafxprojectgenerator.enums.ModuleSystemType;
import com.wedasoft.javafxprojectgenerator.exceptions.NotValidException;
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

    public MainViewControllerService(
            MainViewController viewController) {

        this.viewController = viewController;
    }

    public void onResetButtonClick(
            @SuppressWarnings("unused") ActionEvent event) {

        viewController.getApplicationNameTextField().setText("");
        viewController.getGroupIdTextField().setText("");
        viewController.getModuleSystemTypeChoiceBox().setValue(viewController.getModuleSystemTypeChoiceBox().getItems().get(0));
        viewController.getUsePreconfiguredDatabaseCheckbox().setSelected(true);
        viewController.getUsePreconfiguredDatabaseVersionTextfield().setText("");
        viewController.getUseLibraryWedasoftCheckbox().setSelected(true);
        viewController.getUseLibraryWedasoftVersionTextfield().setText("");
        viewController.getUseLibraryLombokCheckbox().setSelected(true);
        viewController.getUseLibraryLombokVersionTextfield().setText("");
        viewController.getDestinationDirectoryTextField().setText("");
    }

    public void onMenuItemCloseClick() {
        JfxDialogUtil.displayExitProgramDialog();
    }

    public void onChooseDestinationDirectoryButtonClick(
            @SuppressWarnings("unused") ActionEvent event) {

        DirectoryChooser dc = new DirectoryChooser();
        dc.setTitle("Choose the destination directory");
        dc.setInitialDirectory(of(System.getProperty("user.home")).toFile());
        Window ownerWindow = ((Node) event.getSource()).getScene().getWindow();
        File file = dc.showDialog(ownerWindow);
        if (file != null) {
            viewController.getDestinationDirectoryTextField().setText(file.getAbsolutePath());
        }
    }

    public void onCreateProjectButtonClick(
            @SuppressWarnings("unused") ActionEvent event) {

        try {
            ProjectDataDto projectDataDto = new ProjectDataDto(
                    viewController.getApplicationNameTextField().getText(),
                    viewController.getGroupIdTextField().getText(),
                    viewController.getVersionTextField().getText(),
                    viewController.getModuleSystemTypeChoiceBox().getValue(),
                    viewController.getDestinationDirectoryTextField().getText());

            prepareAppDataDirInUserHome();
            extractProjectTemplateFromZip(projectDataDto);
            modifyProjectTemplateFiles(projectDataDto);
            moveProjectTemplateFilesToCorrectPackages(projectDataDto);
            moveCreatedProjectToDestinationDir(projectDataDto);

            JfxDialogUtil.createInformationDialog("Project created successfully.").showAndWait();
        } catch (NotValidException nve) {
            JfxDialogUtil.createErrorDialog(nve.getMessage()).showAndWait();
        } catch (Exception e) {
            JfxDialogUtil.createErrorDialog("Could not create the project.", e);
            e.printStackTrace();
        }
    }

    private void prepareAppDataDirInUserHome()
            throws Exception {

        Files.createDirectories(userHomeApplicationPath);
        FileUtils.cleanDirectory(userHomeApplicationPath.toFile());
    }

    private void extractProjectTemplateFromZip(
            ProjectDataDto projectDataDto)
            throws Exception {

        ZipService.getInstance().extractZipFileFromClassPath(
                MainApplicationLauncher.class,
                projectDataDto.getModuleSystemType().getClassPathOfZipFile(),
                userHomeApplicationPath);
    }

    private void modifyProjectTemplateFiles(
            ProjectDataDto projectDataDto)
            throws IOException, NotValidException {

        if (projectDataDto.getModuleSystemType() != ModuleSystemType.NON_MODULAR) {
            throw new NotValidException("Only the non modular module system is supported yet.");
        }
        modifySettingsGradle();
        modifyBuildGradle();
        modifyMainApplicationLauncherJava();
        modifyMainApplicationJava();
        modifyMainViewControllerJava();
        modifyMainViewFxml();
    }

    private void moveProjectTemplateFilesToCorrectPackages(
            ProjectDataDto projectDataDto)
            throws IOException {

        Path resourcesPath = getPathTo(srcMainResources);
        Path javaPath = getPathTo(srcMainJava);

        List<String> groupIdParts = Arrays.stream(projectDataDto.getGroupId().split("\\.")).toList();
        for (String part : groupIdParts) {
            resourcesPath = resourcesPath.resolve(part);
            javaPath = javaPath.resolve(part);
        }
        resourcesPath = resourcesPath.resolve(projectDataDto.getAppName().toLowerCase());
        javaPath = javaPath.resolve(projectDataDto.getAppName().toLowerCase());

        FileUtils.moveDirectory(getPathTo(srcMainResourcesYourGroupIdJavafxappnonmodular).toFile(), resourcesPath.toFile());
        FileUtils.moveDirectory(getPathTo(srcMainJavaYourGroupIdJavafxappnonmodular).toFile(), javaPath.toFile());

        FileUtils.deleteDirectory(getPathTo(srcMainResourcesYour).toFile());
        FileUtils.deleteDirectory(getPathTo(srcMainJavaYour).toFile());
    }

    private void moveCreatedProjectToDestinationDir(
            ProjectDataDto projectDataDto)
            throws IOException {

        FileUtils.moveDirectory(
                userHomeApplicationPath.resolve(of("JavaFxAppNonModular")).toFile(),
                Paths.get(projectDataDto.getNewProjectDestinationDirPath()).toFile());
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

    private Path getPathTo(
            String[] dirPathPartsToMainViewFxml) {

        String[] zipFilePathParts = viewController.getModuleSystemTypeChoiceBox().getValue().getClassPathOfZipFile().split("/");
        String tmpProjectName = zipFilePathParts[zipFilePathParts.length - 1];
        String tmpZipProjectName = tmpProjectName.substring(0, tmpProjectName.lastIndexOf('.'));

        return userHomeApplicationPath.resolve(of(tmpZipProjectName, dirPathPartsToMainViewFxml));
    }

}
