package ru.itis.logic.services.impl;

import ru.itis.dao.entities.News;
import ru.itis.dao.entities.Project;
import ru.itis.dao.entities.Tag;
import ru.itis.dao.entities.User;
import ru.itis.dao.repositories.TagRepository;
import ru.itis.logic.services.TagService;

import java.util.List;

public class TagServiceImpl implements TagService {

    private final TagRepository tagRepository;

    public TagServiceImpl(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    @Override
    public List<Tag> getList(User current) {
        return tagRepository.findAll(current);
    }

    @Override
    public List<Tag> getList(Project project) {
        return tagRepository.findAll(project);
    }

    @Override
    public List<Tag> getList(News news) {
        return tagRepository.findAll(news);
    }

    @Override
    public List<Tag> getAll() {
        return tagRepository.findAll();
    }
}
