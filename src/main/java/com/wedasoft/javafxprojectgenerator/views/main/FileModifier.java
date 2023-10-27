package com.wedasoft.javafxprojectgenerator.views.main;

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

    public void modifyPomXml(
            ProjectDataDto projectDataDto)
            throws IOException {

        if (!Files.exists(getPathToFile(pomXml))) {
            return;
        }

        Map<String, String> replacements = Map.ofEntries(
                Map.entry("<groupId>your.groupId</groupId>",
                        String.format("<groupId>%s</groupId>",
                                projectDataDto.getGroupId())),
                Map.entry("<artifactId>JavaFxAppMavenNonModular</artifactId>",
                        String.format("<artifactId>%s</artifactId>",
                                projectDataDto.getAppName())),
                Map.entry("<version>1.0.1</version>",
                        String.format("<version>%s</version>",
                                projectDataDto.getVersion())),
                Map.entry("<name>JavaFxAppMavenNonModular</name>",
                        String.format("<name>%s</name>",
                                projectDataDto.getAppName())),
                Map.entry("<mainClassNameParam>your.groupId.javafxappnonmodular.MainApplicationLauncher</mainClassNameParam>",
                        String.format("<mainClassNameParam>%s.%s.MainApplicationLauncher</mainClassNameParam>",
                                projectDataDto.getGroupId(), projectDataDto.getAppName().toLowerCase())));
        modifyAndWriteFile(getPathToFile(pomXml), replacements);
    }

    public void modifySettingsGradle(
            ProjectDataDto projectDataDto)
            throws IOException {

        if (!Files.exists(getPathToFile(settingsGradle))) {
            return;
        }

        Map<String, String> replacements = Map.ofEntries(
                Map.entry("rootProject.name = \"JavaFxAppNonModular\"",
                        "rootProject.name = \"" + projectDataDto.getAppName() + "\""));
        modifyAndWriteFile(getPathToFile(settingsGradle), replacements);
    }

    public void modifyBuildGradle(
            ProjectDataDto projectDataDto)
            throws IOException {

        if (!Files.exists(getPathToFile(buildGradle))) {
            return;
        }

        Map<String, String> replacements = Map.ofEntries(
                Map.entry("group 'com.wedasoft'",
                        String.format("group '%s'",
                                projectDataDto.getGroupId())),
                Map.entry("version '1.0.0'",
                        String.format("version '%s'",
                                projectDataDto.getVersion())),
                Map.entry("mainClassNameParam = 'your.groupId.javafxappnonmodular.MainApplicationLauncher'",
                        String.format("mainClassNameParam = '%s.%s.MainApplicationLauncher'",
                                projectDataDto.getGroupId(), projectDataDto.getAppName().toLowerCase())));
        modifyAndWriteFile(getPathToFile(buildGradle), replacements);
    }

    public void modifyMainApplicationLauncherJava(
            ProjectDataDto projectDataDto)
            throws IOException {

        Map<String, String> replacements = Map.ofEntries(
                Map.entry("package your.groupId.javafxappnonmodular;",
                        String.format("package %s.%s;",
                                projectDataDto.getGroupId(), projectDataDto.getAppName().toLowerCase())));
        modifyAndWriteFile(getPathToFile(srcMainJavaYourGroupIdJavafxappnonmodularMainApplicationLauncherJava), replacements);
    }

    public void modifyMainApplicationJava(
            ProjectDataDto projectDataDto)
            throws IOException {

        Map<String, String> replacements = Map.ofEntries(
                Map.entry("package your.groupId.javafxappnonmodular;",
                        String.format("package %s.%s;",
                                projectDataDto.getGroupId(), projectDataDto.getAppName().toLowerCase())));
        modifyAndWriteFile(getPathToFile(srcMainJavaYourGroupIdJavafxappnonmodularMainApplicationJava), replacements);
    }

    public void modifyMainViewControllerJava(
            ProjectDataDto projectDataDto)
            throws IOException {

        Map<String, String> replacements = Map.ofEntries(
                Map.entry("package your.groupId.javafxappnonmodular.views;",
                        String.format("package %s.%s.views;",
                                projectDataDto.getGroupId(), projectDataDto.getAppName().toLowerCase())));
        modifyAndWriteFile(getPathToFile(srcMainJavaYourGroupIdJavafxappnonmodularViewsMainViewControllerJava), replacements);
    }

    public void modifyMainViewFxml(
            ProjectDataDto projectDataDto)
            throws IOException {

        Map<String, String> replacements = Map.ofEntries(
                Map.entry("your.groupId.javafxappnonmodular.views.MainViewController",
                        String.format("%s.%s.views.MainViewController",
                                projectDataDto.getGroupId(), projectDataDto.getAppName().toLowerCase())));
        modifyAndWriteFile(getPathToFile(srcMainResourcesYourGroupIdJavafxappnonmodularViewsMainViewFxml), replacements);
    }

    public void removeGitDirectory()
            throws IOException {

        removeDirectoryIfExists(getPathToFile(dotGit));
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

    private void removeDirectoryIfExists(
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
