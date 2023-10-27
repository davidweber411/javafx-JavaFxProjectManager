package com.wedasoft.javafxprojectgenerator.enums;

import lombok.Getter;

@Getter
public enum ProjectType {

    GRADLE_NON_MODULAR(
            "Gradle (non modular)",
            "/com/wedasoft/javafxprojectgenerator/zips/JavaFxAppNonModular.zip",
            "/com/wedasoft/javafxprojectgenerator/zips/bmsFilesForGradle.zip",
            "JavaFxAppNonModular"),
    MAVEN_NON_MODULAR(
            "Maven (non modular)",
            "/com/wedasoft/javafxprojectgenerator/zips/JavaFxAppNonModular.zip",
            "/com/wedasoft/javafxprojectgenerator/zips/bmsFilesForMaven.zip",
            "JavaFxAppNonModular");

    private final String uiText;

    private final String classPathOfTemplateProjectZip;

    private final String classPathOfBmsFilesZip;

    private final String templateProjectName;

    ProjectType(
            String uiText,
            String classPathOfTemplateProjectZip,
            String classPathOfBmsFilesZip,
            String templateProjectName) {

        this.uiText = uiText;
        this.classPathOfTemplateProjectZip = classPathOfTemplateProjectZip;
        this.classPathOfBmsFilesZip = classPathOfBmsFilesZip;
        this.templateProjectName = templateProjectName;
    }

    @SuppressWarnings("unused")
    public static ProjectType getByValue(
            String value) {

        for (ProjectType projectType : ProjectType.values()) {
            if (projectType.getUiText().equals(value)) {
                return projectType;
            }
        }
        return null;
    }

    public String getExtractedProjectDirName() {
        String[] zipFilePathParts = classPathOfTemplateProjectZip.split("/");
        String tmpProjectName = zipFilePathParts[zipFilePathParts.length - 1];
        return tmpProjectName.substring(0, tmpProjectName.lastIndexOf('.'));
    }

    public String getExtractedBmsFilesDirName() {
        String[] zipFilePathParts = classPathOfBmsFilesZip.split("/");
        String tmpProjectName = zipFilePathParts[zipFilePathParts.length - 1];
        return tmpProjectName.substring(0, tmpProjectName.lastIndexOf('.'));
    }

}
