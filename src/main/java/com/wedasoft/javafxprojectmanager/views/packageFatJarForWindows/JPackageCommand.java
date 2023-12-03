package com.wedasoft.javafxprojectmanager.views.packageFatJarForWindows;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@SuppressWarnings("UnusedReturnValue")
public class JPackageCommand {

    private String jPackageExePath;
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

    private JPackageCommand(String jPackageExePath) {
        this.jPackageExePath = jPackageExePath;
    }

    public static JPackageCommand createJPackageCommandBuilder(String jPackageExePath) {
        return new JPackageCommand(jPackageExePath);
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

    public String getCompleteCommandAsString() {
        return String.join(" ", getCompleteCommandAsList());
    }

    public List<String> getCompleteCommandAsList() {
        List<String> command = new ArrayList<>();
        command.add(jPackageExePath);
        command.addAll(getArgumentsAsList());
        return command;
    }

    private List<String> getArgumentsAsList() {
        List<String> arguments = new ArrayList<>();
        Arrays.stream(getArguments().split(" --"))
                .filter(e -> !e.isBlank() && !e.isEmpty())
                .map(e -> "--" + e)
                .forEach((e) -> Collections.addAll(arguments, e.split(" ", 2)));
        return arguments;
    }

    private String getArguments() {
        String arguments = "";
        if (typeArg != null) arguments += typeArg;
        if (destArg != null) arguments += destArg;
        if (mainJarArg != null) arguments += mainJarArg;
        if (inputArg != null) arguments += inputArg;
        if (mainClassArg != null) arguments += mainClassArg;
        if (nameArg != null) arguments += nameArg;
        if (appVersionArg != null) arguments += appVersionArg;
        if (iconArg != null) arguments += iconArg;
        if (useWinDirChooser) arguments += " --win-dir-chooser";
        if (useWinShortcut) arguments += " --win-shortcut";
        if (useWinMenu) arguments += " --win-menu";
        return arguments;
    }

}
