package com.wedasoft.javafxprojectmanager.views.packageFatJarForWindows;

import java.nio.file.Path;

@SuppressWarnings("UnusedReturnValue")
public class JPackageCommand {

    private String typeArg;
    private String destArg;
    private String mainJarArg;
    private String inputArg;
    private String mainClassArg;
    private String nameArg;
    private String appVersionArg;
    private String iconArg;
    private boolean useWinDirChooser;
    private boolean useWinShortcut;
    private boolean useWinMenu;

    private JPackageCommand() {
    }

    public static JPackageCommand createJPackageCommandBuilder() {
        return new JPackageCommand();
    }

    public JPackageCommand setTypeArg(String applicationTypeArg) {
        this.typeArg = " --type " + "\"" + applicationTypeArg + "\"";
        return this;
    }

    public JPackageCommand setDestArg(String outputDestinationPath) {
        this.destArg = " --dest " + "\"" + outputDestinationPath + "\"";
        return this;
    }

    public JPackageCommand setMainJarArg(String mainJarPath) {
        this.mainJarArg = " --main-jar " + "\"" + Path.of(mainJarPath).getFileName().toString() + "\"";
        return this;
    }

    public JPackageCommand setInputArg(String dirContainingFilesToPackagePath) {
        this.inputArg = " --input " + "\"" + dirContainingFilesToPackagePath + "\"";
        return this;
    }

    public JPackageCommand setMainClassArg(String path) {
        this.mainClassArg = " --main-class " + "\"" + path + "\"";
        return this;
    }

    public JPackageCommand setNameArg(String nameArg) {
        this.nameArg = " --name " + "\"" + nameArg + "\"";
        return this;
    }

    public JPackageCommand setAppVersionArg(String appVersionArg) {
        this.appVersionArg = " --app-version " + "\"" + appVersionArg + "\"";
        return this;
    }

    public JPackageCommand setIconArg(String iconPathArg) {
        this.iconArg = " --icon " + "\"" + iconPathArg + "\"";
        return this;
    }

    public JPackageCommand setUseWinDirChooser(boolean useWinDirChooser) {
        this.useWinDirChooser = useWinDirChooser;
        return this;
    }

    public JPackageCommand setUseWinShortcut(boolean useWinShortcut) {
        this.useWinShortcut = useWinShortcut;
        return this;
    }

    public JPackageCommand setUseWinMenu(boolean useWinMenu) {
        this.useWinMenu = useWinMenu;
        return this;
    }

    public String getCommand() {
        String jpc = "jpackage";
        if (typeArg != null) jpc += typeArg;
        if (destArg != null) jpc += destArg;
        if (mainJarArg != null) jpc += mainJarArg;
        if (inputArg != null) jpc += inputArg;
        if (mainClassArg != null) jpc += mainClassArg;
        if (nameArg != null) jpc += nameArg;
        if (appVersionArg != null) jpc += appVersionArg;
        if (iconArg != null) jpc += iconArg;
        if (useWinDirChooser) jpc += " --win-dir-chooser";
        if (useWinShortcut) jpc += " --win-shortcut";
        if (useWinMenu) jpc += " --win-menu";
        return jpc;
    }

}
