package com.wedasoft.javafxprojectgenerator.helper;

import java.nio.file.Path;

public class PathConstants {

    /*
    Other
     */
    public static final Path userHomeAppDataDir =
            Path.of(System.getProperty("user.home"), "Wedasoft", "JavaFxProjectGenerator");


    /*
    GIT
     */
    public static final String[] dotGit =
            {".git"};

    /*
    Gradle
     */
    public static final String[] buildGradle =
            {"build.gradle"};

    public static final String[] settingsGradle =
            {"settings.gradle"};


    /*
    Resources
     */
    public static final String[] srcMainResources =
            {"src", "main", "resources"};

    public static final String[] srcMainResourcesYour =
            {"src", "main", "resources", "your"};

    public static final String[] srcMainResourcesYourGroupIdJavafxappnonmodular =
            {"src", "main", "resources", "your", "groupId", "javafxappnonmodular"};

    public static final String[] srcMainResourcesYourGroupIdJavafxappnonmodularViewsMainViewFxml =
            {"src", "main", "resources", "your", "groupId", "javafxappnonmodular", "views", "main-view.fxml"};

    /*
    Java source
     */
    public static final String[] srcMainJava =
            {"src", "main", "java"};

    public static final String[] srcMainJavaYour =
            {"src", "main", "java", "your"};

    public static final String[] srcMainJavaYourGroupIdJavafxappnonmodular =
            {"src", "main", "java", "your", "groupId", "javafxappnonmodular"};

    public static final String[] srcMainJavaYourGroupIdJavafxappnonmodularMainApplicationJava =
            {"src", "main", "java", "your", "groupId", "javafxappnonmodular", "MainApplication.java"};

    public static final String[] srcMainJavaYourGroupIdJavafxappnonmodularMainApplicationLauncherJava =
            {"src", "main", "java", "your", "groupId", "javafxappnonmodular", "MainApplicationLauncher.java"};

    public static final String[] srcMainJavaYourGroupIdJavafxappnonmodularViewsMainViewControllerJava =
            {"src", "main", "java", "your", "groupId", "javafxappnonmodular", "views", "MainViewController.java"};

}
