package ru.itis.logic.services.impl;

import ru.itis.dao.entities.News;
import ru.itis.dao.entities.NewsComment;
import ru.itis.dao.entities.Project;
import ru.itis.dao.entities.ProjectComment;
import ru.itis.dao.repositories.CommentRepository;
import ru.itis.logic.services.CommentService;

import java.util.List;

public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;

    public CommentServiceImpl(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    @Override
    public List<NewsComment> getList(News news) {
        return commentRepository.findAll(news);
    }

    @Override
    public List<ProjectComment> getList(Project project) {
        return commentRepository.findAll(project);
    }

    @Override
    public void like(Long commentId) {
        commentRepository.like(commentId);
    }

    @Override
    public void dislike(Long commentId) {
        commentRepository.dislike(commentId);
    }
}
