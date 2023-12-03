package com.wedasoft.javafxprojectmanager.enums;

public enum PackageContentType {

    SINGLE_JAR_ONLY(1, "Single JAR only (fat JAR / uber JAR)"), ALL_FILES_FROM_DIR(2, "All files in selected directory");

    private final int value;
    private final String label;

    PackageContentType(int value, String label) {
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
