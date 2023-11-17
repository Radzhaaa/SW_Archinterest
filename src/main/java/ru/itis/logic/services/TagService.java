package ru.itis.logic.services;

import ru.itis.dao.entities.News;
import ru.itis.dao.entities.Project;
import ru.itis.dao.entities.Tag;
import ru.itis.dao.entities.User;

import java.util.List;

public interface TagService {
    List<Tag> getList(User current);
    List<Tag> getList(Project project);
    List<Tag> getList(News news);
    List<Tag> getAll();
}
