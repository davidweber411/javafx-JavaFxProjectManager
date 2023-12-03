package com.wedasoft.javafxprojectmanager.enums;

public enum AppTypeToCreate {

    EXE(1, "app-image", "(EXE) Direct executable Application without installation"),
    MSI(2, "msi", "(MSI) Application installer");

    private final int value;
    private final String jPackageArgumentValue;
    private final String label;

    AppTypeToCreate(int value, String jPackageArgumentValue, String label) {
        this.value = value;
        this.jPackageArgumentValue = jPackageArgumentValue;
        this.label = label;
    }

    @SuppressWarnings("unused")
    public int getValue() {
        return value;
    }

    public String getJPackageArgumentValue() {
        return jPackageArgumentValue;
    }

    public String getLabel() {
        return label;
    }

}
