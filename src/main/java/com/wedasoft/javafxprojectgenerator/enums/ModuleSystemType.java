package com.wedasoft.javafxprojectgenerator.enums;

import lombok.Getter;

@Getter
public enum ModuleSystemType {

    NON_MODULAR("non modular");

    private final String value;

    ModuleSystemType(String value) {
        this.value = value;
    }

}
