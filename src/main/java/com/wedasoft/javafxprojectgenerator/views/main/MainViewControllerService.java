package com.wedasoft.javafxprojectgenerator.views.main;

import com.wedasoft.javafxprojectgenerator.MainApplicationLauncher;
import com.wedasoft.javafxprojectgenerator.exceptions.NotValidException;
import com.wedasoft.javafxprojectgenerator.helper.FileHelper;
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
        viewController.getProjectTypeChoiceBox().setValue(viewController.getProjectTypeChoiceBox().getItems().get(0));
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
                    viewController.getProjectTypeChoiceBox().getValue(),
                    viewController.getDestinationDirectoryTextField().getText());

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
            JfxDialogUtil.createErrorDialog("Could not create the project.", e);
            e.printStackTrace();
        }
    }

    private void prepareAppDataDirInUserHome()
            throws Exception {

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
            FileHelper.copyDirContent(bmsFilesZipDir, bmsFilesZipDir.getParent(), true);
        }
        FileHelper.deleteDir(bmsFilesZipDir, true);
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

}
