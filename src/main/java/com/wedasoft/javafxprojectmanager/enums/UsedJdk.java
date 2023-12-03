package com.wedasoft.javafxprojectmanager.enums;

public enum UsedJdk {

    WRAPPED_OPEN_JDK_17(1, "Wrapped Open JDK 17"),
    CONFIGURE_OWN_JDK(2, "Configure own JDK");

    private final int value;
    private final String label;

    UsedJdk(int value, String label) {
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
