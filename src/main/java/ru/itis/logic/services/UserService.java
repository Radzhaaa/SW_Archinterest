package ru.itis.logic.services;

import ru.itis.dao.entities.Tag;
import ru.itis.dao.entities.User;

import javax.servlet.http.Part;
import java.util.List;

public interface UserService {
    User get(Long id);
    User get(String username, String password);

    void create(String username, String email, String hash);

    void update(User user);

    void update(User profile, List<Tag> tags);

    void update(User user, Part avatar);
}
