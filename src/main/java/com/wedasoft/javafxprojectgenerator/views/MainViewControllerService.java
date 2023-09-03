package com.wedasoft.javafxprojectgenerator.views;

import com.wedasoft.javafxprojectgenerator.MainApplicationLauncher;
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

@SuppressWarnings("ClassCanBeRecord")
@Getter
public class MainViewControllerService {

    private static final String END_OF_LINE_TOKEN = "//willBeInitilizedByJavaFxProjectGenerator";

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
        if (viewController.getApplicationNameTextField().getText().isBlank()) {
            JfxDialogUtil.createErrorDialog("You must enter an application name.").showAndWait();
            return;
        }
        if (viewController.getGroupIdTextField().getText().isBlank()) {
            JfxDialogUtil.createErrorDialog("You must enter group id.").showAndWait();
            return;
        }
        if (viewController.getVersionTextField().getText().isBlank()) {
            JfxDialogUtil.createErrorDialog("You must enter a version.").showAndWait();
            return;
        }
        if (viewController.getModuleSystemTypeChoiceBox().getValue() == null) {
            JfxDialogUtil.createErrorDialog("You must select a module system type.").showAndWait();
            return;
        }
        if (viewController.getDestinationDirectoryTextField().getText().isBlank()) {
            JfxDialogUtil.createErrorDialog("You must specify a target destination path.").showAndWait();
            return;
        }

        Path destinationDirectoryPath = Paths.get(viewController.getDestinationDirectoryTextField().getText());
        if (Files.isRegularFile(destinationDirectoryPath)) {
            JfxDialogUtil.createErrorDialog("The specified target directory must not be a file.").showAndWait();
            return;
        }
        Path projectToCreatePath = destinationDirectoryPath.resolve(Path.of(viewController.getApplicationNameTextField().getText()));
        if (Files.exists(projectToCreatePath)) {
            JfxDialogUtil.createErrorDialog("There already exists an project with the name '" + viewController.getApplicationNameTextField().getText() + "' in the target directory.").showAndWait();
            return;
        }

        try {
            // create the tmp directory if it does not exist yet
            Path userHomeWedasoftPath = Path.of(System.getProperty("user.home"), "Wedasoft", "JavaFxProjectGenerator");
            Files.createDirectories(userHomeWedasoftPath);

            // clear the tmp directory
            FileUtils.cleanDirectory(userHomeWedasoftPath.toFile());

            // extract the template project to a tmp directory
            ZipService.getInstance().extractZipFileFromClassPath(
                    MainApplicationLauncher.class,
                    "/com/wedasoft/javafxprojectgenerator/zips/JavaFxAppNonModular.zip",
                    userHomeWedasoftPath);

            // modify the projects files

            // move the project to the destination directory
            FileUtils.moveDirectory(
                    userHomeWedasoftPath.resolve(Path.of("JavaFxAppNonModular")).toFile(),
                    projectToCreatePath.getParent().resolve(Path.of(viewController.getApplicationNameTextField().getText())).toFile());

            // success message
            JfxDialogUtil.createInformationDialog("Project created successfully.");
        } catch (Exception e) {
            JfxDialogUtil.createErrorDialog("Could not create the project.", e);
            e.printStackTrace();
        }
    }


}
