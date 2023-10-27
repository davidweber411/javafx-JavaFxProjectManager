package com.wedasoft.javafxprojectgenerator.views.main;

import com.wedasoft.javafxprojectgenerator.enums.ProjectType;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.Map;
import java.util.stream.Stream;

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
            modifyAndWriteFile(
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
            modifyAndWriteFile(
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
            modifyAndWriteFile(
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
            modifyAndWriteFile(
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
            modifyAndWriteFile(
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
            modifyAndWriteFile(
                    getPathToFile(srcMainResourcesYourGroupIdJavafxappnonmodularViewsMainViewFxml),
                    Map.ofEntries(
                            Map.entry("your.groupId.javafxappnonmodular.views.MainViewController",
                                    String.format("%s.%s.views.MainViewController",
                                            projectDataDto.getGroupId(), projectDataDto.getAppName().toLowerCase()))));
        }
    }

    public void removeGitDirectory()
            throws IOException {

        removeDirectory(getPathToFile(dotGit));
    }

    private void modifyAndWriteFile(
            Path pathToFile,
            Map<String, String> oldStringToNewStringReplacementsMap)
            throws IOException {

        String fileContent = FileUtils.readFileToString(pathToFile.toFile(), Charset.defaultCharset());
        for (Map.Entry<String, String> entry : oldStringToNewStringReplacementsMap.entrySet()) {
            System.out.println("entry.key=" + entry.getKey());
            fileContent = fileContent.replace(entry.getKey(), entry.getValue());
        }
        FileUtils.write(pathToFile.toFile(), fileContent, Charset.defaultCharset());
    }

    private void removeDirectory(
            Path pathToDir)
            throws IOException {

        File file = pathToDir.toFile();
        if (file.exists()) {
            try (Stream<Path> pathStream = Files.walk(pathToDir)) {
                //noinspection ResultOfMethodCallIgnored
                pathStream.sorted(Comparator.reverseOrder())
                        .map(Path::toFile)
                        .forEach(File::delete);
            }
        }
    }

}
