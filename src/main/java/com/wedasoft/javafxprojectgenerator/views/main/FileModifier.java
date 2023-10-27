package com.wedasoft.javafxprojectgenerator.views.main;

import com.wedasoft.javafxprojectgenerator.enums.ProjectType;
import com.wedasoft.javafxprojectgenerator.services.FileModificationService;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Map;

import static com.wedasoft.javafxprojectgenerator.helper.PathConstants.*;
import static com.wedasoft.javafxprojectgenerator.helper.PathConstants.srcMainResourcesYourGroupIdJavafxappnonmodularViewsMainViewFxml;
import static java.nio.file.Path.of;

public class FileModifier {

    private final ProjectDataDto projectDataDto;

    public FileModifier(ProjectDataDto projectDataDto) {
        this.projectDataDto = projectDataDto;
    }

    private Path getPathToFile(
            String[] dirPathPartsToMainViewFxml) {

        return userHomeAppDataDir.resolve(of(
                projectDataDto.getProjectType().getTemplateProjectName(),
                dirPathPartsToMainViewFxml));
    }

    public void modifySettingsGradle(
            ProjectDataDto projectDataDto)
            throws IOException {

        if (projectDataDto.getProjectType() == ProjectType.GRADLE_NON_MODULAR) {
            FileModificationService.getInstance().modifyAndWriteFile(
                    getPathToFile(settingsGradle),
                    Map.ofEntries(
                            Map.entry("rootProject.name = \"JavaFxAppNonModular\" //willBeInitilizedByJavaFxProjectGenerator",
                                    "rootProject.name = \"" + projectDataDto.getAppName() + "\"")));
        }
    }

    public void modifyBuildGradle(
            ProjectDataDto projectDataDto)
            throws IOException {

        if (projectDataDto.getProjectType() == ProjectType.GRADLE_NON_MODULAR) {
            FileModificationService.getInstance().modifyAndWriteFile(
                    getPathToFile(buildGradle),
                    Map.ofEntries(
                            Map.entry("group 'com.wedasoft'",
                                    String.format("group '%s'",
                                            projectDataDto.getGroupId())),
                            Map.entry("version '2.0.0'",
                                    String.format("version '%s'",
                                            projectDataDto.getVersion())),
                            Map.entry("mainClassNameParam = 'your.groupId.javafxappnonmodular.MainApplicationLauncher'",
                                    String.format("mainClassNameParam = '%s.%s.MainApplicationLauncher'",
                                            projectDataDto.getGroupId(), projectDataDto.getAppName().toLowerCase()))));
        }
    }

    public void modifyMainApplicationLauncherJava(
            ProjectDataDto projectDataDto)
            throws IOException {

        if (projectDataDto.getProjectType() == ProjectType.GRADLE_NON_MODULAR) {
            FileModificationService.getInstance().modifyAndWriteFile(
                    getPathToFile(srcMainJavaYourGroupIdJavafxappnonmodularMainApplicationLauncherJava),
                    Map.ofEntries(
                            Map.entry("package your.groupId.javafxappnonmodular;",
                                    String.format("package %s.%s;",
                                            projectDataDto.getGroupId(), projectDataDto.getAppName().toLowerCase()))));
        }
    }

    public void modifyMainApplicationJava(
            ProjectDataDto projectDataDto)
            throws IOException {

        if (projectDataDto.getProjectType() == ProjectType.GRADLE_NON_MODULAR) {
            FileModificationService.getInstance().modifyAndWriteFile(
                    getPathToFile(srcMainJavaYourGroupIdJavafxappnonmodularMainApplicationJava),
                    Map.ofEntries(
                            Map.entry("package your.groupId.javafxappnonmodular;",
                                    String.format("package %s.%s;",
                                            projectDataDto.getGroupId(), projectDataDto.getAppName().toLowerCase()))));
        }
    }

    public void modifyMainViewControllerJava(
            ProjectDataDto projectDataDto)
            throws IOException {

        if (projectDataDto.getProjectType() == ProjectType.GRADLE_NON_MODULAR) {
            FileModificationService.getInstance().modifyAndWriteFile(
                    getPathToFile(srcMainJavaYourGroupIdJavafxappnonmodularViewsMainViewControllerJava),
                    Map.ofEntries(
                            Map.entry("package your.groupId.javafxappnonmodular.views;",
                                    String.format("package %s.%s.views;",
                                            projectDataDto.getGroupId(), projectDataDto.getAppName().toLowerCase()))));
        }
    }

    public void modifyMainViewFxml(
            ProjectDataDto projectDataDto)
            throws IOException {

        if (projectDataDto.getProjectType() == ProjectType.GRADLE_NON_MODULAR) {
            FileModificationService.getInstance().modifyAndWriteFile(
                    getPathToFile(srcMainResourcesYourGroupIdJavafxappnonmodularViewsMainViewFxml),
                    Map.ofEntries(
                            Map.entry("your.groupId.javafxappnonmodular.views.MainViewController",
                                    String.format("%s.%s.views.MainViewController",
                                            projectDataDto.getGroupId(), projectDataDto.getAppName().toLowerCase()))));
        }
    }

}
