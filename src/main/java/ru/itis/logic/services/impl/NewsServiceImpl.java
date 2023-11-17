package ru.itis.logic.services.impl;

import ru.itis.dao.entities.News;
import ru.itis.dao.entities.NewsComment;
import ru.itis.dao.entities.Tag;
import ru.itis.dao.entities.User;
import ru.itis.dao.repositories.NewsRepository;
import ru.itis.dao.repositories.TagRepository;
import ru.itis.logic.services.NewsService;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class NewsServiceImpl implements NewsService {

    private final NewsRepository newsRepository;
    private final TagRepository tagRepository;

    public NewsServiceImpl(NewsRepository newsRepository, TagRepository tagRepository) {
        this.newsRepository = newsRepository;
        this.tagRepository = tagRepository;
    }

    @Override
    public List<News> getAll() {
        return newsRepository.findAll();
    }

    @Override
    public List<News> getAllToday() {
        return newsRepository.getAllBetween(getStartOfDay(), getEndOfDay());
    }

    @Override
    public List<News> getAllToday(User currentUser) {
        return newsRepository.getAllBetweenByTags(getStartOfDay(), getEndOfDay(), currentUser);
    }

    @Override
    public News get(Long id) {
        return newsRepository.get(id);
    }

    @Override
    public void create(News news) {
        newsRepository.save(news);
    }

    @Override
    public void update(News news, NewsComment comment) {
        newsRepository.update(news, comment);
    }

    @Override
    public void update(News news, List<Tag> tagsToUpdate) {
        newsRepository.update(news, tagsToUpdate);
    }

    @Override
    public News get(News news) {
        return newsRepository.get(news);
    }

    @Override
    public List<News> getAll(User current) {
        return newsRepository.findAll(current);
    }

    private Timestamp getStartOfDay() {
        LocalDate now = LocalDate.now();
        return Timestamp.valueOf(LocalDateTime.of(now.getYear(),
                now.getMonth(),
                now.getDayOfMonth(),
                0, 0, 0));
    }
    private Timestamp getEndOfDay() {
        LocalDate now = LocalDate.now();
        return Timestamp.valueOf(LocalDateTime.of(now.getYear(),
                now.getMonth(),
                now.getDayOfMonth(),
                23, 59, 59));
    }
}
