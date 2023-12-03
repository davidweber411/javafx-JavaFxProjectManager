package com.wedasoft.javafxprojectmanager.helper;

import java.io.FileNotFoundException;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileHelper {

    public static Path copyDirContent(
            Path contentContainingDir,
            Path copyIntoDirPath,
            boolean overwriteExistingFiles)
            throws Exception {

        if (contentContainingDir == null) {
            throw new IllegalArgumentException(
                    "Directory content could not be copied. The path of the content containing directory to copy must not be null.");
        }
        if (copyIntoDirPath == null) {
            throw new IllegalArgumentException(
                    "Directory content could not be copied. The path of the directory, into which the directory content shall be copied, must not be null.");
        }
        if (!Files.isDirectory(contentContainingDir)) {
            throw new IllegalArgumentException(
                    "Directory content could not be copied. The path of the directory which contains the content to copy either does not exist or it does not represent a directory.");
        }

        Files.createDirectories(copyIntoDirPath);
        Files.walkFileTree(contentContainingDir,
                new CopyFileVisitor(contentContainingDir, copyIntoDirPath, overwriteExistingFiles));

        return copyIntoDirPath;
    }

    public static void deleteDir(
            Path pathOfDirToDelete,
            boolean throwIfDirNotExists)
            throws Exception {

        if (Files.isDirectory(pathOfDirToDelete)) {
            Files.walkFileTree(pathOfDirToDelete, new DeleteFileVisitor());
        } else if (throwIfDirNotExists) {
            throw new FileNotFoundException("The directory could not be deleted because it does not exist.");
        }
    }

}

