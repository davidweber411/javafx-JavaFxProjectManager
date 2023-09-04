package com.wedasoft.javafxprojectgenerator.services;

import org.apache.commons.io.FileUtils;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Map;
import java.util.regex.Matcher;

public class FileModificationService {

    private static FileModificationService service;

    private FileModificationService() {
    }

    public static synchronized FileModificationService getInstance() {
        if (service == null) {
            service = new FileModificationService();
        }
        return service;
    }

    @SuppressWarnings("unused")
    public synchronized String[] splitFilePathToParts(String path) {
        String s = Arrays.deepToString(path.split(Matcher.quoteReplacement(System.getProperty("file.separator"))));
        s = s.substring(1);
        s = s.substring(0, s.length() - 1);
        return s.split(", ");
    }

    public synchronized void modifyAndWriteFile(Path pathToFile, Map<String, String> oldStringToNewStringReplacementsMap) throws IOException {
        String fileContent = FileUtils.readFileToString(pathToFile.toFile(), Charset.defaultCharset());
        for (Map.Entry<String, String> entry : oldStringToNewStringReplacementsMap.entrySet()) {
            System.out.println("entry.key=" + entry.getKey());
            fileContent = fileContent.replace(entry.getKey(), entry.getValue());
        }
        FileUtils.write(pathToFile.toFile(), fileContent, Charset.defaultCharset());
    }


}
