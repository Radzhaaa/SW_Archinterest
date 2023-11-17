package ru.itis.logic.services;

import ru.itis.dao.entities.News;
import ru.itis.dao.entities.NewsComment;
import ru.itis.dao.entities.Project;
import ru.itis.dao.entities.ProjectComment;

import java.util.List;

public interface CommentService {
    List<NewsComment> getList(News news);

    List<ProjectComment> getList(Project project);

    void like(Long commentId);
    void dislike(Long commentId);
}
