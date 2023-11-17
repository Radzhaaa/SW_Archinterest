package ru.itis.logic.services.impl;

import ru.itis.dao.entities.Image;
import ru.itis.dao.repositories.ImageRepository;
import ru.itis.logic.services.ImageService;
import ru.itis.utils.FileUploader;

import javax.servlet.http.Part;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class ImageServiceImpl implements ImageService {

    private final ImageRepository imageRepository;
    private static final String DIRECTORY_PATH = "/Users/alinaradzhapbaeva/Desktop/archinterest/src/main/webapp/images/";


    public ImageServiceImpl(ImageRepository imageRepository) {
        this.imageRepository = imageRepository;
    }

    @Override
    public void create(Part file) {
        try (InputStream inputStream = file.getInputStream()) {
            imageRepository.create(file);
            FileUploader.upload(inputStream, DIRECTORY_PATH, file.getSubmittedFileName());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void create(List<Image> images) {
        for (Image image : images) {
            imageRepository.create(image);
        }
    }

    @Override
    public Image get(Part file) {
        return imageRepository.find(file.getSubmittedFileName());
    }

    @Override
    public List<Image> findAll() {
        return imageRepository.findAll();
    }
}
