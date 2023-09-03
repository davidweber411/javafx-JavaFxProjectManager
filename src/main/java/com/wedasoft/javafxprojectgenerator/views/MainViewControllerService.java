package com.wedasoft.javafxprojectgenerator.views;

import lombok.Getter;

@Getter
public class MainViewControllerService {

    private static final String END_OF_LINE_TOKEN = "//willBeInitilizedByJavaFxProjectGenerator";

    private final MainViewController controller;

    public MainViewControllerService(MainViewController controller) {
        this.controller = controller;
    }


//    @SuppressWarnings({"ResultOfMethodCallIgnored"})
//    public static void createAppDataDirs() {
//        File appDataTmpDirPath = new File(APP_DATA_TMP_DIR_PATH);
//        if (!appDataTmpDirPath.exists()) {
//            appDataTmpDirPath.mkdirs();
//        }
//        File appDataIncludedJdkDirPath = new File(APP_DATA_INCLUDED_JDK_DIR_PATH);
//        if (!appDataIncludedJdkDirPath.exists()) {
//            appDataIncludedJdkDirPath.mkdirs();
//        }
//    }
//
//    private static void extractZippedJdk17() {
//        for (String zipFilePath : List.of("/com/wedasoft/java2nativeWinConverter/included-openjdk-17-windows-x64-part1.zip", "/com/wedasoft/java2nativeWinConverter/included-openjdk-17-windows-x64-part2.zip")) {
//            InputStream is = MainApplicationLauncher.class.getResourceAsStream(zipFilePath);
//            ZipInputStream zis = new ZipInputStream(requireNonNull(is));
//            try {
//                ZipEntry entry;
//                while ((entry = zis.getNextEntry()) != null) {
//                    String filePath = APP_DATA_ROOT_PATH + File.separator + entry.getName();
//                    if (!entry.isDirectory()) {
//                        // if the entry is a file, extract it
//                        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(filePath));
//                        byte[] bytesIn = new byte[1024];
//                        int read;
//                        while ((read = zis.read(bytesIn)) != -1) {
//                            bos.write(bytesIn, 0, read);
//                        }
//                        bos.close();
//                    } else {
//                        // if the entry is a directory, make the directory
//                        File dir = new File(filePath);
//                        //noinspection ResultOfMethodCallIgnored
//                        dir.mkdirs();
//                    }
//                    zis.closeEntry();
//                }
//                zis.close();
//                is.close();
//            } catch (Exception e) {
//                JOptionPane.showMessageDialog(null, "Could not extract the zipped part of the JDK 17.");
//            }
//        }
//
//        Path extractedZipPart1 = Path.of(APP_DATA_ROOT_PATH, "included-openjdk-17-windows-x64-part1");
//        Path extractedZipPart2 = Path.of(APP_DATA_ROOT_PATH, "included-openjdk-17-windows-x64-part2");
//        Path includedJdk17Path = Path.of(APP_DATA_INCLUDED_JDK_DIR_PATH);
//        try {
//            FileSystemUtils.copyDirContent(extractedZipPart1, includedJdk17Path, true);
//            FileSystemUtils.copyDirContent(extractedZipPart2, includedJdk17Path, true);
//            FileSystemUtils.deleteDir(extractedZipPart1, false);
//            FileSystemUtils.deleteDir(extractedZipPart2, false);
//        } catch (Exception e) {
//            JOptionPane.showMessageDialog(null, "Could not combine the extracted parts of the JDK 17.");
//            throw new RuntimeException(e);
//        }
//    }

}
