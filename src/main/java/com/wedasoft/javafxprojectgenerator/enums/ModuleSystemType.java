package com.wedasoft.javafxprojectgenerator.enums;

import lombok.Getter;

@Getter
public enum ModuleSystemType {

    NON_MODULAR("non modular", "/com/wedasoft/javafxprojectgenerator/zips/JavaFxAppNonModular.zip");

    private final String value;

    private final String classPathOfZipFile;

    ModuleSystemType(String value, String classPathOfZipFile) {
        this.value = value;
        this.classPathOfZipFile = classPathOfZipFile;
    }

    @SuppressWarnings("unused")
    public static ModuleSystemType getByValue(String value) {
        for (ModuleSystemType moduleSystemType : ModuleSystemType.values()) {
            if (moduleSystemType.getValue().equals(value)) {
                return moduleSystemType;
            }
        }
        return null;
    }

}
