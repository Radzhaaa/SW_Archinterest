package ru.itis.logic.services.impl;

import ru.itis.dao.entities.Project;
import ru.itis.dao.entities.ProjectComment;
import ru.itis.dao.entities.Tag;
import ru.itis.dao.entities.User;
import ru.itis.dao.repositories.ProjectRepository;
import ru.itis.logic.services.ProjectService;

import java.util.List;

public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;

    public ProjectServiceImpl(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    @Override
    public List<Project> getList(User current) {
        return projectRepository.findAllByUser(current);
    }

    @Override
    public void create(Project project) {
        projectRepository.create(project);
    }

    @Override
    public List<Project> getInterested(User current) {
        return projectRepository.findAll(current);
    }

    @Override
    public void update(Project project, ProjectComment comment) {
        projectRepository.update(project, comment);
    }

    @Override
    public Project get(Project project) {
        return projectRepository.get(project);
    }

    @Override
    public void update(Project project, List<Tag> tagsToUpdate) {
        projectRepository.update(project, tagsToUpdate);
    }

    @Override
    public Project get(Long projectId) {
        return projectRepository.get(projectId);
    }
}
