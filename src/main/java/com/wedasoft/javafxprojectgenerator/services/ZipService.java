package com.wedasoft.javafxprojectgenerator.services;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class ZipService {

    private static ZipService service;

    private ZipService() {
    }

    public static synchronized ZipService getInstance() {
        if (service == null) {
            service = new ZipService();
        }
        return service;
    }

    public synchronized void extractZipFileFromClassPath(
            Class<?> mainClassOfApplication,
            String classPathOfZipFile,
            Path targetExtractionPath)
            throws Exception {

        InputStream inputStream = mainClassOfApplication.getResourceAsStream(classPathOfZipFile);
        ZipInputStream zipInputStream = new ZipInputStream(Objects.requireNonNull(inputStream));

        try {
            ZipEntry zipEntry;
            while ((zipEntry = zipInputStream.getNextEntry()) != null) {
                Path resolvedPath = targetExtractionPath.resolve(zipEntry.getName()).normalize();
                if (!resolvedPath.startsWith(targetExtractionPath)) {
                    throw new RuntimeException("Entry with an illegal path: " + zipEntry.getName());
                }
                if (zipEntry.isDirectory()) {
                    Files.createDirectories(resolvedPath);
                } else {
                    Files.createDirectories(resolvedPath.getParent());
                    Files.copy(zipInputStream, resolvedPath);
                }
            }
        } catch (Exception ex) {
            throw new Exception(ex);
        } finally {
            zipInputStream.closeEntry();
            zipInputStream.close();
            inputStream.close();
        }

    }

}
