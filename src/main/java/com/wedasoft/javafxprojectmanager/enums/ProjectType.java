package com.wedasoft.javafxprojectmanager.enums;

public enum ProjectType {

    GRADLE_NON_MODULAR(
            "Gradle (non modular)",
            "/com/wedasoft/javafxprojectmanager/zips/JavaFxAppNonModular.zip",
            "/com/wedasoft/javafxprojectmanager/zips/bmsFilesForGradle.zip",
            "JavaFxAppNonModular"),
    MAVEN_NON_MODULAR(
            "Maven (non modular)",
            "/com/wedasoft/javafxprojectmanager/zips/JavaFxAppNonModular.zip",
            "/com/wedasoft/javafxprojectmanager/zips/bmsFilesForMaven.zip",
            "JavaFxAppNonModular");

    public final String uiText;

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
    public static ProjectType getByValue(String value) {
        for (ProjectType projectType : ProjectType.values()) {
            if (projectType.uiText.equals(value)) {
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

    public String getUiText() {
        return uiText;
    }

    public String getClassPathOfTemplateProjectZip() {
        return classPathOfTemplateProjectZip;
    }

    public String getClassPathOfBmsFilesZip() {
        return classPathOfBmsFilesZip;
    }

    public String getTemplateProjectName() {
        return templateProjectName;
    }
}
