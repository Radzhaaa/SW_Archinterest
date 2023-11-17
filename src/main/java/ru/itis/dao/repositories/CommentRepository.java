package ru.itis.dao.repositories;

import ru.itis.dao.entities.News;
import ru.itis.dao.entities.NewsComment;
import ru.itis.dao.entities.Project;
import ru.itis.dao.entities.ProjectComment;

import java.util.List;

public interface CommentRepository {
    List<NewsComment> findAll(News news);
    List<ProjectComment> findAll(Project project);
    void like(Long commentId);
    void dislike(Long commentId);
}
