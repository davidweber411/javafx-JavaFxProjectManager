package com.wedasoft.javafxprojectgenerator.enums;

import lombok.Getter;

@Getter
public enum ProjectType {

    GRADLE_NON_MODULAR(
            "Gradle (non modular)",
            "/com/wedasoft/javafxprojectgenerator/zips/JavaFxAppGradleNonModularV1.0.0.zip",
            "JavaFxAppGradleNonModular");

    private final String uiText;

    private final String classPathOfZipFile;

    private final String templateProjectName;

    ProjectType(
            String uiText,
            String classPathOfZipFile,
            String templateProjectName) {

        this.uiText = uiText;
        this.classPathOfZipFile = classPathOfZipFile;
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

    //    public String getTemplateProjectName() {
    //        String[] zipFilePathParts = classPathOfZipFile.split("/");
    //        String tmpProjectName = zipFilePathParts[zipFilePathParts.length - 1];
    //
    //        return tmpProjectName.substring(0, tmpProjectName.lastIndexOf('.'));
    //    }

}
