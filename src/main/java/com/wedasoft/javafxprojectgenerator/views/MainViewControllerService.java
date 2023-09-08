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
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

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
        dc.setInitialDirectory(Path.of(System.getProperty("user.home")).toFile());
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
        if (Files.exists(getProjectToCreatePath())) {
            throw new NotValidException("There already exists an project with the name '"
                    + viewController.getApplicationNameTextField().getText() + "' in the target directory.");
        }
    }

    private Path getProjectToCreatePath() {
        return Paths.get(viewController.getDestinationDirectoryTextField().getText())
                .resolve(Path.of(viewController.getApplicationNameTextField().getText()));
    }

    private Path getUserHomeWedasoftJavaFxProjectGeneratorPath() {
        return Path.of(System.getProperty("user.home"), "Wedasoft", "JavaFxProjectGenerator");
    }

    private void createProject() throws Exception {
        // create the tmp directory if it does not exist yet, clean it, extract zip
        Files.createDirectories(getUserHomeWedasoftJavaFxProjectGeneratorPath());
        FileUtils.cleanDirectory(getUserHomeWedasoftJavaFxProjectGeneratorPath().toFile());
        ZipService.getInstance().extractZipFileFromClassPath(
                MainApplicationLauncher.class,
                viewController.getModuleSystemTypeChoiceBox().getValue().getClassPathOfZip(),
                getUserHomeWedasoftJavaFxProjectGeneratorPath());

        // modify the projects files
        if (viewController.getModuleSystemTypeChoiceBox().getValue() == ModuleSystemType.NON_MODULAR) {
            Path tmpProjectPath = getUserHomeWedasoftJavaFxProjectGeneratorPath().resolve(getTmpProjectName());

            FileModificationService.getInstance().modifyAndWriteFile(tmpProjectPath.resolve("settings.gradle"),
                    Map.ofEntries(
                            Map.entry("rootProject.name = \"JavaFxAppNonModular\" //willBeInitilizedByJavaFxProjectGenerator",
                                    "rootProject.name = \"" + viewController.getApplicationNameTextField().getText() + "\"")));

            FileModificationService.getInstance().modifyAndWriteFile(tmpProjectPath.resolve("build.gradle"),
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

            FileModificationService.getInstance().modifyAndWriteFile(tmpProjectPath.resolve("src").resolve("main")
                            .resolve("java").resolve("your").resolve("groupId").resolve("javafxappnonmodular")
                            .resolve("MainApplicationLauncher.java"),
                    Map.ofEntries(
                            Map.entry("package your.groupId.javafxappnonmodular;",
                                    String.format("package %s.%s;",
                                            viewController.getGroupIdTextField().getText(), viewController.getApplicationNameTextField().getText().toLowerCase()))));

            FileModificationService.getInstance().modifyAndWriteFile(tmpProjectPath.resolve("src").resolve("main")
                            .resolve("java").resolve("your").resolve("groupId").resolve("javafxappnonmodular")
                            .resolve("MainApplication.java"),
                    Map.ofEntries(
                            Map.entry("package your.groupId.javafxappnonmodular;",
                                    String.format("package %s.%s;",
                                            viewController.getGroupIdTextField().getText(), viewController.getApplicationNameTextField().getText().toLowerCase()))));

            FileModificationService.getInstance().modifyAndWriteFile(tmpProjectPath.resolve("src").resolve("main")
                            .resolve("java").resolve("your").resolve("groupId").resolve("javafxappnonmodular")
                            .resolve("views").resolve("MainViewController.java"),
                    Map.ofEntries(
                            Map.entry("package your.groupId.javafxappnonmodular.views;",
                                    String.format("package %s.%s.views;",
                                            viewController.getGroupIdTextField().getText(), viewController.getApplicationNameTextField().getText().toLowerCase()))));

            FileModificationService.getInstance().modifyAndWriteFile(tmpProjectPath.resolve("src").resolve("main")
                            .resolve("resources").resolve("your").resolve("groupId").resolve("javafxappnonmodular")
                            .resolve("views").resolve("main-view.fxml"),
                    Map.ofEntries(
                            Map.entry("your.groupId.javafxappnonmodular.views.MainViewController",
                                    String.format("%s.%s.views.MainViewController",
                                            viewController.getGroupIdTextField().getText(), viewController.getApplicationNameTextField().getText().toLowerCase()))));

            List<String> groupIdDirectories = Arrays.stream(viewController.getGroupIdTextField().getText().split("\\.")).toList();

            Path javaDirPath = tmpProjectPath.resolve("src").resolve("main").resolve("java");
            for (String directory : groupIdDirectories) {
                javaDirPath = javaDirPath.resolve(directory);
            }
            javaDirPath = javaDirPath.resolve(viewController.getApplicationNameTextField().getText().toLowerCase());
            FileUtils.moveDirectory(tmpProjectPath.resolve("src").resolve("main").resolve("java")
                            .resolve("your").resolve("groupId").resolve("javafxappnonmodular").toFile(),
                    javaDirPath.toFile());
            FileUtils.deleteDirectory(tmpProjectPath.resolve("src").resolve("main").resolve("java").resolve("your").toFile());

            Path resourcesDirPath = tmpProjectPath.resolve("src").resolve("main").resolve("resources");
            for (String directory : groupIdDirectories) {
                resourcesDirPath = resourcesDirPath.resolve(directory);
            }
            resourcesDirPath = resourcesDirPath.resolve(viewController.getApplicationNameTextField().getText().toLowerCase());
            FileUtils.moveDirectory(tmpProjectPath.resolve("src").resolve("main").resolve("resources")
                            .resolve("your").resolve("groupId").resolve("javafxappnonmodular").toFile(),
                    resourcesDirPath.toFile());
            FileUtils.deleteDirectory(tmpProjectPath.resolve("src").resolve("main").resolve("resources").resolve("your").toFile());
        }

        FileUtils.moveDirectory(
                getUserHomeWedasoftJavaFxProjectGeneratorPath().resolve(Path.of("JavaFxAppNonModular")).toFile(),
                getProjectToCreatePath().getParent().resolve(Path.of(viewController.getApplicationNameTextField().getText())).toFile());
        JfxDialogUtil.createInformationDialog("Project created successfully.").showAndWait();
    }


    private String getTmpProjectName() {
        String[] parts = viewController.getModuleSystemTypeChoiceBox().getValue().getClassPathOfZip().split("/");
        String tmpProjectName = parts[parts.length - 1];
        tmpProjectName = tmpProjectName.substring(0, tmpProjectName.lastIndexOf('.'));
        return tmpProjectName;
    }

}
