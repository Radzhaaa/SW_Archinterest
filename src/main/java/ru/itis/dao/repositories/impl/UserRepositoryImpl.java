package ru.itis.dao.repositories.impl;

import ru.itis.dao.entities.Tag;
import ru.itis.dao.entities.User;
import ru.itis.dao.repositories.UserRepository;
import ru.itis.dao.utils.JdbcUtil;
import ru.itis.dao.utils.RowMapper;

import java.sql.Connection;
import java.util.List;

public class UserRepositoryImpl implements UserRepository {

    private final Connection connection;
    private final JdbcUtil<User> jdbcUtil;

    public UserRepositoryImpl(Connection connection) {
        this.connection = connection;
        this.jdbcUtil = new JdbcUtil<>();
    }

    private final RowMapper<User> userRowMapper = (row, number) -> User.builder()
            .id(row.getLong("id"))
            .name(row.getString("name"))
            .avatarPath(row.getString("avatar_path"))
            .patronymic(row.getString("patronymic"))
            .lastname(row.getString("lastname"))
            .username(row.getString("username"))
            .email(row.getString("email"))
            .password(row.getString("password"))
            .about(row.getString("about"))
            .registeredAt(row.getTimestamp("registered_at"))
            .build();

    @Override
    public User get(Long id) {
        String selectSql = "select * from account where id = %s";
        selectSql = String.format(selectSql, id);
        return jdbcUtil.selectOne(connection, selectSql, userRowMapper);
    }

    @Override
    public User get(String username, String password) {
        String selectSql = "select * from account where username = '%s' and password = '%s'";
        selectSql = String.format(selectSql, username, password);
        return jdbcUtil.selectOne(connection, selectSql, userRowMapper);
    }

    @Override
    public void update(User user) {
        String updateSql = "update account set name = '%s', patronymic = '%s', lastname = '%s', about = '%s', email = '%s' where id = %s";
        updateSql = String.format(updateSql, user.getName(), user.getPatronymic(), user.getLastname(), user.getAbout(), user.getEmail(), user.getId());
        JdbcUtil.execute(connection, updateSql);
    }

    @Override
    public void update(User profile, List<Tag> tags) {
        update(profile);

        if (!tags.isEmpty()) {
            String deleteSql = "delete from account_tag where account_id = %s;";
            deleteSql = String.format(deleteSql, profile.getId());
            JdbcUtil.execute(connection, deleteSql);

            StringBuilder createSql = new StringBuilder(" insert into account_tag (account_id, tag_id) values ");

            for (Tag tag : tags) {
                createSql.append(String.format("(%s, %s), ", profile.getId(), tag.getId()));
            }

            createSql = new StringBuilder(createSql.substring(0, createSql.length() - 2)).append(";");
            JdbcUtil.execute(connection, createSql.toString());
        }
    }

    @Override
    public void update(User profile, String avatarPath) {
        String updateSql = String.format("update account set avatar_path = '%s'", avatarPath);
        JdbcUtil.execute(connection, updateSql);
    }

    @Override
    public void create(User user) {
        String createSql = "insert into account (username, email, password, registered_at) values ('%s', '%s', '%s', now())";
        createSql = String.format(createSql, user.getUsername(), user.getEmail(), user.getPassword());
        JdbcUtil.execute(connection, createSql);
    }
}
