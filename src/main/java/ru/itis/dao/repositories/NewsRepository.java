package ru.itis.dao.repositories;

import ru.itis.dao.entities.News;
import ru.itis.dao.entities.NewsComment;
import ru.itis.dao.entities.Tag;
import ru.itis.dao.entities.User;

import java.sql.Timestamp;
import java.util.List;

public interface NewsRepository {
    List<News> findAll();
    List<News> findAll(User current);
    List<News> getAllBetween(Timestamp from, Timestamp to);
    List<News> getAllBetweenByTags(Timestamp startOfDay, Timestamp endOfDay, User currentUser);
    News get(Long id);
    void save(News news);
    void update(News news, NewsComment comment);
    void update(News news, List<Tag> tagsToUpdate);
    News get(News news);
}
