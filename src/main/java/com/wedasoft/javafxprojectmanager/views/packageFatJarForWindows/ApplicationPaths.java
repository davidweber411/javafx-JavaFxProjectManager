package com.wedasoft.javafxprojectmanager.views.packageFatJarForWindows;

import java.nio.file.Path;

public class ApplicationPaths {

    public static final Path APP_DATA_PATH = Path.of(
            System.getProperty("user.home"),
            "Wedasoft",
            "JavaFxProjectManager");

    public static final Path APP_DATA_TMP_PATH = APP_DATA_PATH.resolve("tmp");

    public static final Path APP_DATA_INCLUDED_JDK17_PATH = APP_DATA_PATH.resolve("included-openjdk-17-windows-x64");

    public static final Path APP_DATA_INCLUDED_JPACKAGE_EXE_PATH = APP_DATA_INCLUDED_JDK17_PATH
            .resolve("bin")
            .resolve("jpackage.exe");

}
