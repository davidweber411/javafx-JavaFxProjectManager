package com.wedasoft.javafxprojectmanager.enums;

public enum UsedJpackage {

    FROM_WRAPPED_OPEN_JDK_17(1, "Use jPackage EXE from wrapped Open JDK 17"),
    FROM_CONFIGURED_JDK(2, "Use jPackage EXE from configured JDK");

    private final int value;
    private final String label;

    UsedJpackage(int value, String label) {
        this.value = value;
        this.label = label;
    }

    @SuppressWarnings("unused")
    public int getValue() {
        return value;
    }

    public String getLabel() {
        return label;
    }

}
