package com.wedasoft.javafxprojectmanager.views.packageFatJarForWindows;

import com.wedasoft.javafxprojectmanager.enums.AppTypeToCreate;
import com.wedasoft.javafxprojectmanager.enums.MainClassReferenceType;
import com.wedasoft.javafxprojectmanager.enums.PackageContentType;
import com.wedasoft.javafxprojectmanager.exceptions.NotValidException;
import com.wedasoft.simpleJavaFxApplicationBase.jfxDialogs.JfxDialogUtil;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

import java.util.List;

import static com.wedasoft.javafxprojectmanager.views.packageFatJarForWindows.ApplicationPaths.APP_DATA_TMP_PATH;

public class PackageFatJarForWindowsViewControllerService {

    public static void displayMenuItemHelp() {
        JfxDialogUtil.createInformationDialog("""
                        # Working explanation\r
                        This software is just a GUI application, that uses jpackage (software from Oracle JDK 17) under the hood.\r
                        If you want to use this software, you must configure jpackage correctly. \r
                        \r
                        # Needed settings for using jpackage\r
                        \r
                        ## Install the WIX-Tools\r
                        The WIX-Tools are needed and must be installed for executing jpackage.\r
                        Download WiX 3.0 or later from https://wixtoolset.org and add it to the PATH.\r
                        \r
                        ## Install .NET-Framework 3.5.1\r
                        The WIX-Tools need the .NET-Framework 3.5.1.\r
                        Depending on your system, this must be configured manually after the installation at "Windows-Icon-Button > Windows-Features".\r
                        To that, the PATH variable (from the environment variables) must be extended with the "/bin" path.\r
                        .NET-Framework can be installed by installing "Visual Studio (Community Edition)". Make sure to choose .NET (App and C# Desktop) during the installation.\r
                        \r
                        # jpackage documentation\r
                        All parameters for jpackage can be found here:\r
                        https://docs.oracle.com/en/java/javase/14/docs/specs/man/jpackage.html""")
                .showAndWait();
    }

    public static void displayMenuItemAbout() {
        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(10, 10, 10, 10));
        gridPane.setVgap(8);
        gridPane.setHgap(10);

        Label appNameLabel = new Label("Application name:");
        GridPane.setConstraints(appNameLabel, 0, 0);
        Label appName = new Label("Java 2 Native Windows Converter");
        GridPane.setConstraints(appName, 1, 0);
        Label progLangLabel = new Label("Used programming language:");
        GridPane.setConstraints(progLangLabel, 0, 1);
        Label progLang = new Label("Java");
        GridPane.setConstraints(progLang, 1, 1);
        Label authorLabel = new Label("Created by:");
        GridPane.setConstraints(authorLabel, 0, 2);
        Label author = new Label("David Weber");
        GridPane.setConstraints(author, 1, 2);
        Label creationDateLabel = new Label("Creation date:");
        GridPane.setConstraints(creationDateLabel, 0, 3);
        Label creationDate = new Label("2022-05-29");
        GridPane.setConstraints(creationDate, 1, 3);

        gridPane.getChildren().addAll(appNameLabel, appName, progLangLabel, progLang, authorLabel, author, creationDateLabel, creationDate);

        JfxDialogUtil.createDialogWithColumns("About", 1, 0, List.of(gridPane));
    }

    public static void displayExitDialog() {
        Alert alert = new Alert(Alert.AlertType.NONE);
        alert.setTitle("Exit program");
        alert.setContentText("Do you really want to exit the program?");
        alert.getButtonTypes().add(ButtonType.OK);
        alert.getButtonTypes().add(ButtonType.CANCEL);
        alert.showAndWait();
        if (alert.getResult().getButtonData() == ButtonBar.ButtonData.OK_DONE) {
            Platform.exit();
            System.exit(0);
        }
    }

    public static String createJPackageCommandString(String typeArg, String destArg, String mainJarArg,
                                                     PackageContentType selectedPackageContentType, String inputArg,
                                                     String mainClassArg, String nameArg, String appVersionArg,
                                                     String iconArg, boolean useWinDirChooser, boolean useWinShortcut,
                                                     boolean useWinMenu, MainClassReferenceType selectedMainClassReferenceType,
                                                     AppTypeToCreate selectedAppTypeToCreate) throws NotValidException {
        JPackageCommand jpc = JPackageCommand.createJPackageCommandBuilder();
        jpc.setTypeArg(typeArg);

        if (destArg.isEmpty()) {
            throw new NotValidException("You must specify the directory, in which the generated files should be placed.");
        }
        jpc.setDestArg(destArg);

        if (mainJarArg.isEmpty()) {
            throw new NotValidException("You must specify the path to the jar file which contains the main method. This can be done with an absolute or a relative path.");
        }
        jpc.setMainJarArg(mainJarArg);

        if (selectedPackageContentType.equals(PackageContentType.SINGLE_JAR_ONLY)) {
            jpc.setInputArg(APP_DATA_TMP_PATH.toString());
        } else if (inputArg.isEmpty()) {
            throw new NotValidException("You must enter the path of the directory, in which all files including the main JAR, which shall be packaged, are located.");
        } else {
            jpc.setInputArg(inputArg);
        }

        if (!selectedMainClassReferenceType.equals(MainClassReferenceType.MAINCLASS_OF_MAINJAR)) {
            if (mainClassArg.isEmpty()) {
                throw new NotValidException("If there is a deviating main class, you must specify it.");
            } else {
                jpc.setMainClassArg(mainClassArg);
            }
        }

        if (nameArg.isEmpty()) {
            throw new NotValidException("You must enter a name for your application.");
        }
        jpc.setNameArg(nameArg);

        if (appVersionArg.isEmpty()) {
            throw new NotValidException("You must enter a version number for your application, like \"1.3\".");
        }
        jpc.setAppVersionArg(appVersionArg);

        if (!iconArg.isEmpty()) {
            jpc.setIconArg(iconArg);
        }

        if (selectedAppTypeToCreate.equals(AppTypeToCreate.MSI)) {
            if (useWinDirChooser) {
                jpc.setUseWinDirChooser(true);
            }
            if (useWinShortcut) {
                jpc.setUseWinShortcut(true);
            }
            if (useWinMenu) {
                jpc.setUseWinMenu(true);
            }
        }

        return jpc.getCommand();
    }
}
