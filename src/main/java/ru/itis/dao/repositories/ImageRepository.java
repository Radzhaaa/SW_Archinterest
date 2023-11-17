package ru.itis.dao.repositories;

import ru.itis.dao.entities.Image;

import javax.servlet.http.Part;
import java.util.List;

public interface ImageRepository {

    Image find(String path);
    void create(Part file);

    List<Image> findAll();

    void create(Image image);
}
