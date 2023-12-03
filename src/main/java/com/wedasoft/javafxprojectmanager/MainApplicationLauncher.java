package com.wedasoft.javafxprojectmanager;

import com.wedasoft.javafxprojectmanager.helper.ZipHelper;
import com.wedasoft.simpleJavaFxApplicationBase.fileSystemUtil.FileSystemUtil;

import javax.swing.*;
import java.io.IOException;
import java.nio.file.Files;

import static com.wedasoft.javafxprojectmanager.views.packageFatJarForWindows.ApplicationPaths.*;
import static java.nio.file.Path.of;

public class MainApplicationLauncher {

    public static void main(String[] args) {
        createAppDataDirs();
        unzipWrappedJdk17();

        MainApplication.main(args);
    }

    private static void unzipWrappedJdk17() {
        try {
            FileSystemUtil.clearDir(APP_DATA_PATH.resolve(of("included-openjdk-17-windows-x64")));
            ZipHelper.unzipFile(
                    MainApplicationLauncher.class.getResourceAsStream("/com/wedasoft/javafxprojectmanager/zips/included-openjdk-17-windows-x64-part1.zip"),
                    APP_DATA_PATH.resolve(of("included-openjdk-17-windows-x64")));
            ZipHelper.unzipFile(
                    MainApplicationLauncher.class.getResourceAsStream("/com/wedasoft/javafxprojectmanager/zips/included-openjdk-17-windows-x64-part2.zip"),
                    APP_DATA_PATH.resolve(of("included-openjdk-17-windows-x64")));
            FileSystemUtil.copyDirContent(
                    APP_DATA_PATH.resolve(of("included-openjdk-17-windows-x64")).resolve("included-openjdk-17-windows-x64-part1"),
                    APP_DATA_PATH.resolve(of("included-openjdk-17-windows-x64")).resolve("included-openjdk-17-windows-x64-part1").getParent(),
                    true);
            FileSystemUtil.copyDirContent(
                    APP_DATA_PATH.resolve(of("included-openjdk-17-windows-x64")).resolve("included-openjdk-17-windows-x64-part2"),
                    APP_DATA_PATH.resolve(of("included-openjdk-17-windows-x64")).resolve("included-openjdk-17-windows-x64-part2").getParent(),
                    true);
            FileSystemUtil.deleteDir(APP_DATA_PATH.resolve(of("included-openjdk-17-windows-x64")).resolve("included-openjdk-17-windows-x64-part1"));
            FileSystemUtil.deleteDir(APP_DATA_PATH.resolve(of("included-openjdk-17-windows-x64")).resolve("included-openjdk-17-windows-x64-part2"));
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "An error occured while extracting the wrapped open JDK 17.");
        }
    }

    public static void createAppDataDirs() {
        try {
            if (!Files.exists(APP_DATA_TMP_PATH)) {
                Files.createDirectories(APP_DATA_TMP_PATH);
            }
            if (!Files.exists(APP_DATA_INCLUDED_JDK17_PATH)) {
                Files.createDirectories(APP_DATA_INCLUDED_JDK17_PATH);
            }
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "An error occured while creating the app data directories.");
        }
    }

}