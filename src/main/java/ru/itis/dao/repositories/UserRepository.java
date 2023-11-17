package ru.itis.dao.repositories;

import ru.itis.dao.entities.Tag;
import ru.itis.dao.entities.User;

import java.sql.SQLException;
import java.util.List;

public interface UserRepository {
    User get(Long id);
    User get(String username, String password);
    void create(User user);
    void update(User user);
    void update(User profile, List<Tag> tags);
    void update(User profile, String avatarPath);
}
