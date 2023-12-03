package com.wedasoft.javafxprojectmanager.enums;

@SuppressWarnings("SpellCheckingInspection")
public enum MainClassReferenceType {

    MAINCLASS_OF_MAINJAR(1, "Use main class of main JAR"),
    DEVIATING_MAINCLASS(2, "Use a different class as main class");

    private final int value;
    private final String label;

    MainClassReferenceType(int value, String label) {
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
