package com.wedasoft.javafxprojectmanager.views.createProject;

import com.wedasoft.javafxprojectmanager.MainApplicationLauncher;
import com.wedasoft.javafxprojectmanager.exceptions.NotValidException;
import com.wedasoft.javafxprojectmanager.services.ZipService;
import com.wedasoft.simpleJavaFxApplicationBase.fileSystemUtil.FileSystemUtil;
import com.wedasoft.simpleJavaFxApplicationBase.jfxDialogs.JfxDialogUtil;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
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
import java.util.Objects;

import static com.wedasoft.javafxprojectmanager.helper.PathConstants.*;
import static java.nio.file.Path.of;

@Getter
public class CreateProjectViewControllerService {

    private final CreateProjectViewController controller;

    public CreateProjectViewControllerService(CreateProjectViewController controller) {
        this.controller = controller;
    }

    public void onResetButtonClick(@SuppressWarnings("unused") ActionEvent event) {
        controller.getApplicationNameTextField().setText("");
        controller.getGroupIdTextField().setText("");
        controller.getProjectTypeChoiceBox().setValue(controller.getProjectTypeChoiceBox().getItems().get(0));
        controller.getUsePreconfiguredDatabaseCheckbox().setSelected(true);
        controller.getUsePreconfiguredDatabaseVersionTextfield().setText("");
        controller.getUseLibraryWedasoftCheckbox().setSelected(true);
        controller.getUseLibraryWedasoftVersionTextfield().setText("");
        controller.getUseLibraryLombokCheckbox().setSelected(true);
        controller.getUseLibraryLombokVersionTextfield().setText("");
        controller.getDestinationDirectoryTextField().setText("");
    }

    public void onChooseDestinationDirectoryButtonClick(ActionEvent event) {
        DirectoryChooser dc = new DirectoryChooser();
        dc.setTitle("Choose the destination directory");
        dc.setInitialDirectory(of(System.getProperty("user.home")).toFile());
        Window ownerWindow = ((Node) event.getSource()).getScene().getWindow();
        File file = dc.showDialog(ownerWindow);
        if (file != null) {
            controller.getDestinationDirectoryTextField().setText(file.getAbsolutePath());
        }
    }

    public void onCreateProjectButtonClick(@SuppressWarnings("unused") ActionEvent event) {
        try {
            ProjectDataDto projectDataDto = new ProjectDataDto(
                    controller.getApplicationNameTextField().getText(),
                    controller.getGroupIdTextField().getText(),
                    controller.getVersionTextField().getText(),
                    controller.getProjectTypeChoiceBox().getValue(),
                    controller.getDestinationDirectoryTextField().getText());

            prepareAppDataDirInUserHome();
            extractTemplateProjectFromZip(projectDataDto);
            extractBuildManagementSystemFilesFromZip(projectDataDto);
            modifyProjectTemplateFiles(projectDataDto);
            moveProjectTemplateFilesToCorrectPackages(projectDataDto);
            moveCreatedProjectToDestinationDir(projectDataDto);

            JfxDialogUtil.createInformationDialog("Project created successfully.").showAndWait();
        } catch (NotValidException nve) {
            JfxDialogUtil.createErrorDialog(nve.getMessage()).showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
            JfxDialogUtil.createErrorDialog("Could not create the project.", e).showAndWait();
        }
    }

    private void prepareAppDataDirInUserHome() throws Exception {
        Files.createDirectories(userHomeAppDataDir);
        FileUtils.cleanDirectory(userHomeAppDataDir.toFile());
    }

    private void extractTemplateProjectFromZip(
            ProjectDataDto projectDataDto)
            throws Exception {

        ZipService.getInstance().extractZipFileFromClassPath(
                MainApplicationLauncher.class,
                projectDataDto.getProjectType().getClassPathOfTemplateProjectZip(),
                userHomeAppDataDir);
    }

    private void extractBuildManagementSystemFilesFromZip(
            ProjectDataDto projectDataDto)
            throws Exception {

        Path projectDir = userHomeAppDataDir.resolve(projectDataDto.getProjectType().getExtractedProjectDirName());
        ZipService.getInstance().extractZipFileFromClassPath(
                MainApplicationLauncher.class,
                projectDataDto.getProjectType().getClassPathOfBmsFilesZip(),
                projectDir);

        Path bmsFilesZipDir = projectDir.resolve(projectDataDto.getProjectType().getExtractedBmsFilesDirName());
        if (Files.exists(bmsFilesZipDir)) {
            FileSystemUtil.copyDirContent(bmsFilesZipDir, bmsFilesZipDir.getParent(), true);
        }
        FileSystemUtil.deleteDir(bmsFilesZipDir);
    }

    private void modifyProjectTemplateFiles(
            ProjectDataDto projectDataDto)
            throws IOException, NotValidException {

        FileModifier fileModifier = new FileModifier(projectDataDto);
        fileModifier.modifyPomXml(projectDataDto);
        fileModifier.modifySettingsGradle(projectDataDto);
        fileModifier.modifyBuildGradle(projectDataDto);
        fileModifier.modifyMainApplicationLauncherJava(projectDataDto);
        fileModifier.modifyMainApplicationJava(projectDataDto);
        fileModifier.modifyMainViewControllerJava(projectDataDto);
        fileModifier.modifyMainViewFxml(projectDataDto);
        fileModifier.removeGitDirectory();
    }

    private void moveProjectTemplateFilesToCorrectPackages(
            ProjectDataDto projectDataDto)
            throws IOException {

        Path resourcesPath = getPathToFile(srcMainResources, projectDataDto);
        Path javaPath = getPathToFile(srcMainJava, projectDataDto);

        List<String> groupIdParts = Arrays.stream(projectDataDto.getGroupId().split("\\.")).toList();
        for (String part : groupIdParts) {
            resourcesPath = resourcesPath.resolve(part);
            javaPath = javaPath.resolve(part);
        }
        resourcesPath = resourcesPath.resolve(projectDataDto.getAppName().toLowerCase());
        javaPath = javaPath.resolve(projectDataDto.getAppName().toLowerCase());

        FileUtils.moveDirectory(getPathToFile(srcMainResourcesYourGroupIdJavafxappnonmodular, projectDataDto).toFile(), resourcesPath.toFile());
        FileUtils.moveDirectory(getPathToFile(srcMainJavaYourGroupIdJavafxappnonmodular, projectDataDto).toFile(), javaPath.toFile());

        FileUtils.deleteDirectory(getPathToFile(srcMainResourcesYour, projectDataDto).toFile());
        FileUtils.deleteDirectory(getPathToFile(srcMainJavaYour, projectDataDto).toFile());
    }

    private void moveCreatedProjectToDestinationDir(
            ProjectDataDto projectDataDto)
            throws IOException {

        FileUtils.moveDirectory(
                userHomeAppDataDir.resolve(of(projectDataDto.getProjectType().getTemplateProjectName())).toFile(),
                Paths.get(projectDataDto.getNewProjectDestinationDirPath()).toFile());
    }


    private Path getPathToFile(
            String[] dirPathPartsToMainViewFxml,
            ProjectDataDto projectDataDto) {

        return userHomeAppDataDir.resolve(of(projectDataDto.getProjectType().getTemplateProjectName(), dirPathPartsToMainViewFxml));
    }

    public void onMenuItemHowToImportInEclipseClick() {
        JfxDialogUtil.createDialogWithColumns("About", 3, 10, List.of(
                        new Label("Step 1: Start the import wizard"),
                        new Label("Step 2: Select the build management system"),
                        new Label("Step 3: 'Next' and continue the installer wizard"),
                        new ImageView(Objects.requireNonNull(getClass().getResource(
                                "/com/wedasoft/javafxprojectmanager/images/import-in-eclipse-1.jpg")).toString()),
                        new ImageView(Objects.requireNonNull(getClass().getResource(
                                "/com/wedasoft/javafxprojectmanager/images/import-in-eclipse-2.jpg")).toString())))
                .showAndWait();
    }

}
