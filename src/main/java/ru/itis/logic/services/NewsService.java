package ru.itis.logic.services;

import ru.itis.dao.entities.News;
import ru.itis.dao.entities.NewsComment;
import ru.itis.dao.entities.Tag;
import ru.itis.dao.entities.User;

import java.util.List;

public interface NewsService {
    List<News> getAll();
    List<News> getAll(User current);
    List<News> getAllToday();
    List<News> getAllToday(User currentUser);
    News get(Long id);
    void create(News news);
    void update(News news, NewsComment comment);
    void update(News news, List<Tag> tagsToUpdate);
    News get(News news);
}
