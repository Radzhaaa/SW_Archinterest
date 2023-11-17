package ru.itis.dao.repositories.impl;

import ru.itis.dao.entities.*;
import ru.itis.dao.entities.abs.Comment;
import ru.itis.dao.repositories.ProjectRepository;
import ru.itis.dao.utils.JdbcUtil;
import ru.itis.dao.utils.RowMapper;

import java.sql.Connection;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

public class ProjectRepositoryImpl implements ProjectRepository {

    private final Connection connection;
    private final JdbcUtil<Project> jdbcUtil;

    public ProjectRepositoryImpl(Connection connection) {
        this.connection = connection;
        this.jdbcUtil = new JdbcUtil<>();
    }

    private final RowMapper<Project> projectRowMapper = (row, number) -> Project.builder()
            .id(row.getLong("id"))
            .authorId(row.getLong("author_id"))
            .authorUsername(row.getString("author_username"))
            .title(row.getString("title"))
            .coverPath(row.getString("cover_path"))
            .content(row.getString("content"))
            .postedAt(row.getTimestamp("created_at"))
            .address(row.getString("address"))
            .area(row.getDouble("area"))
            .year(row.getInt("year"))
            .build();

    @Override
    public List<Project> findAllByUser(User current) {
        String selectSql = String.format("select * from project where author_id = %s", current.getId());
        return jdbcUtil.selectList(connection, selectSql, projectRowMapper);
    }

    @Override
    public List<Project> findAll(User current) {
        String selectSql = String.format("select * from project p join project_tag pt on p.id = pt.project_id left join account_tag at on at.tag_id = pt.tag_id where at.account_id = %s", current.getId());
        return jdbcUtil.selectList(connection, selectSql, projectRowMapper);
    }

    @Override
    public void create(Project project) {
        String address = project.getAddress() == null ? "" : project.getAddress();
        String createSql = "insert into project (author_id, author_username, title, content, cover_path, created_at, address, area, year) values (%s, '%s', '%s', '%s', '%s', now(), '%s', %s, %s)";
        createSql = String.format(createSql, project.getAuthorId(), project.getAuthorUsername(), project.getTitle(), project.getContent(), project.getCoverPath(), address, project.getArea(), project.getYear());
        JdbcUtil.execute(connection, createSql);
    }

    @Override
    public Project get(Long projectId) {
        String selectSql = String.format("select * from project p where p.id = %s", projectId);
        return jdbcUtil.selectOne(connection, selectSql, projectRowMapper);
    }

    @Override
    public void update(Project project, ProjectComment comment) {
        Timestamp now = Timestamp.valueOf(LocalDateTime.now());
        String createSql = "insert into comment (author_id, author_username, content, created_at, likes, dislikes,avatar_path) values (%s, '%s', '%s', '%s', 0, 0, '%s')";
        createSql = String.format(createSql, comment.getAuthorId(), comment.getAuthorUsername(), comment.getContent(), now, comment.getAvatarPath());
        JdbcUtil.execute(connection, createSql);

        String selectSql = "select * from comment where author_id = %s and content = '%s' and created_at = '%s'";
        selectSql = String.format(selectSql, comment.getAuthorId(), comment.getContent(), now);
        JdbcUtil<Comment> commentJdbcUtil = new JdbcUtil<>();
        Comment savedComment = commentJdbcUtil.selectOne(connection, selectSql, (row, number) -> NewsComment.builder()
                .id(row.getLong("id"))
                .build());

        String updateSql = String.format("insert into project_comment values (%s, %s)", project.getId(), savedComment.getId());
        JdbcUtil.execute(connection, updateSql);
    }

    @Override
    public void update(Project project, List<Tag> tags) {
        if (!tags.isEmpty()) {
            StringBuilder createSql = new StringBuilder(" insert into project_tag (project_id, tag_id) values ");

            for (Tag tag : tags) {
                createSql.append(String.format("(%s, %s), ", project.getId(), tag.getId()));
            }

            createSql = new StringBuilder(createSql.substring(0, createSql.length() - 2)).append(";");
            JdbcUtil.execute(connection, createSql.toString());
        }
    }

    @Override
    public Project get(Project project) {
        String selectSql = "select * from project where title = '%s' and content = '%s' and author_id = %s";
        selectSql = String.format(selectSql, project.getTitle(), project.getContent(), project.getAuthorId());
        return jdbcUtil.selectOne(connection, selectSql, projectRowMapper);
    }
}
