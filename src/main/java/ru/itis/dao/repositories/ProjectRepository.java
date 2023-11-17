package ru.itis.dao.repositories;

import ru.itis.dao.entities.Project;
import ru.itis.dao.entities.ProjectComment;
import ru.itis.dao.entities.Tag;
import ru.itis.dao.entities.User;

import java.util.List;

public interface ProjectRepository {
    List<Project> findAllByUser(User current);

    List<Project> findAll(User current);

    void create(Project project);

    Project get(Long projectId);
    void update(Project project, ProjectComment comment);
    void update(Project project, List<Tag> tags);
    Project get(Project project);
}
