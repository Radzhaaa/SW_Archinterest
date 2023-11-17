package ru.itis.utils;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class FileUploader {
    private FileUploader() {
    }

    public static void upload(InputStream inputStream, String directoryPath, String fileName) throws IOException {
        String path = directoryPath + fileName;

        byte[] bytes = inputStream.readAllBytes();

        try (FileOutputStream fileOutputStream = new FileOutputStream(path)) {
            fileOutputStream.write(bytes);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
