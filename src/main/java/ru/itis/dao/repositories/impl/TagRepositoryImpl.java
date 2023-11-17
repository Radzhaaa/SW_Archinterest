package ru.itis.dao.repositories.impl;

import ru.itis.dao.entities.News;
import ru.itis.dao.entities.Project;
import ru.itis.dao.entities.Tag;
import ru.itis.dao.entities.User;
import ru.itis.dao.repositories.TagRepository;
import ru.itis.dao.utils.JdbcUtil;
import ru.itis.dao.utils.RowMapper;

import java.sql.Connection;
import java.util.List;

public class TagRepositoryImpl implements TagRepository {

    private final Connection connection;
    private final JdbcUtil<Tag> jdbcUtil;

    public TagRepositoryImpl(Connection connection) {
        this.connection = connection;
        jdbcUtil = new JdbcUtil<>();
    }

    private final RowMapper<Tag> tagRowMapper = (row, number) -> Tag.builder()
            .id(row.getLong("id"))
            .title(row.getString("title"))
            .build();

    @Override
    public List<Tag> findAll(User current) {
        String selectSql = "select * from tag t left join account_tag at on at.tag_id = t.id where at.account_id = %s order by title";
        selectSql = String.format(selectSql, current.getId());
        return jdbcUtil.selectList(connection, selectSql, tagRowMapper);
    }

    @Override
    public List<Tag> findAll(Project project) {
        String selectSql = "select * from tag t left join project_tag at on at.tag_id = t.id where at.project_id = %s order by title";
        selectSql = String.format(selectSql, project.getId());
        return jdbcUtil.selectList(connection, selectSql, tagRowMapper);
    }

    @Override
    public List<Tag> findAll(News news) {
        String selectSql = "select * from tag t left join news_tag at on at.tag_id = t.id where at.news_id = %s order by title";
        selectSql = String.format(selectSql, news.getId());
        return jdbcUtil.selectList(connection, selectSql, tagRowMapper);
    }

    @Override
    public List<Tag> findAll() {
        String selectSql = "select * from tag order by title";
        return jdbcUtil.selectList(connection, selectSql, tagRowMapper);
    }
}
