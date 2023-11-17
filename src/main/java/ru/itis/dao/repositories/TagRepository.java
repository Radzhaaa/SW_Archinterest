package ru.itis.dao.repositories;

import ru.itis.dao.entities.News;
import ru.itis.dao.entities.Project;
import ru.itis.dao.entities.Tag;
import ru.itis.dao.entities.User;

import java.util.List;

public interface TagRepository {
    List<Tag> findAll(User current);

    List<Tag> findAll(Project project);

    List<Tag> findAll(News news);

    List<Tag> findAll();
}
