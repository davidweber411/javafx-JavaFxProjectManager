package com.wedasoft.javafxprojectmanager.views.createProject;

import com.wedasoft.javafxprojectmanager.enums.ProjectType;
import com.wedasoft.javafxprojectmanager.exceptions.NotValidException;
import com.wedasoft.javafxprojectmanager.helper.HelperFunctions;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;

import static java.nio.file.Path.of;

public class ProjectDataDto {

    private final String appName;
    private final String groupId;
    private final String version;
    private final ProjectType projectType;
    private final String destinationDirPath;
    private final String newProjectDestinationDirPath;

    public ProjectDataDto(
            String appName,
            String groupId,
            String version,
            ProjectType projectType,
            String destinationDirPath)
            throws NotValidException {
        this.appName = appName;
        this.groupId = groupId;
        this.version = version;
        this.projectType = projectType;
        this.destinationDirPath = destinationDirPath;
        this.newProjectDestinationDirPath = Paths.get(destinationDirPath).resolve(of(appName)).toString();
        checkValid();
    }

    private void checkValid() throws NotValidException {
        if (appName.isBlank()) {
            throw new NotValidException("You must enter an application name.");
        }
        if (!appName.matches("[a-zA-Z0-9]+")) {
            throw new NotValidException("The application name must only contain the characters a-z, A-Z, 0-9.");
        }

        if (groupId.isBlank()) {
            throw new NotValidException("You must enter group id.");
        }
        if (!groupId.matches("[a-zA-Z0-9.]+")) {
            throw new NotValidException("The group id must only contain the characters a-z, A-Z, 0-9, (dot).");
        }
        if (groupId.charAt(0) == '.') {
            throw new NotValidException("The first character of the group id mustn't be a dot.");
        }
        if (groupId.charAt(groupId.length() - 1) == '.') {
            throw new NotValidException("The last character of the group id mustn't be a dot.");
        }
        if (Arrays.stream(groupId.split("\\."))
                .anyMatch(part -> HelperFunctions.isNumeric(part.charAt(0) + ""))) {
            throw new NotValidException("Group id parts mustn't begin with a number.");
        }

        if (version.isBlank()) {
            throw new NotValidException("You must enter a version.");
        }
        if (projectType == null) {
            throw new NotValidException("You must select a module system type.");
        }
        if (destinationDirPath.isBlank()) {
            throw new NotValidException("You must specify a target destination path.");
        }
        if (Files.isRegularFile(Paths.get(destinationDirPath))) {
            throw new NotValidException("The specified target directory must not be a file.");
        }
        if (Files.exists(Paths.get(newProjectDestinationDirPath))) {
            throw new NotValidException("There already exists an project with the name '"
                    + appName + "' in the target directory.");
        }
    }

    public String getAppName() {
        return appName;
    }

    public String getGroupId() {
        return groupId;
    }

    public String getVersion() {
        return version;
    }

    public ProjectType getProjectType() {
        return projectType;
    }

    public String getDestinationDirPath() {
        return destinationDirPath;
    }

    public String getNewProjectDestinationDirPath() {
        return newProjectDestinationDirPath;
    }

}
