package ru.itis.dao.repositories.impl;

import ru.itis.dao.entities.News;
import ru.itis.dao.entities.NewsComment;
import ru.itis.dao.entities.Tag;
import ru.itis.dao.entities.User;
import ru.itis.dao.entities.abs.Comment;
import ru.itis.dao.repositories.NewsRepository;
import ru.itis.dao.utils.JdbcUtil;
import ru.itis.dao.utils.RowMapper;

import java.sql.Connection;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

public class NewsRepositoryImpl implements NewsRepository {

    private final Connection connection;
    private final JdbcUtil<News> jdbcUtil;

    private static final String SELECT_ALL = "select distinct n.* from news n";
    private static final String LEFT_JOIN = "  join news_tag nt on nt.news_id = n.id";
    private static final String WHERE_BETWEEN = " where n.created_at between '%s' and '%s'";
    private static final String WHERE_ID = " where n.id = %s";
    private static final String WHERE_TAG_IN = " and nt.tag_id in (%s)";
    private static final String ORDER_BY = " order by created_at";

    public NewsRepositoryImpl(Connection connection) {
        this.connection = connection;
        this.jdbcUtil = new JdbcUtil<>();
    }

    private final RowMapper<News> newsRowMapper = (row, number) -> News.builder()
            .id(row.getLong("id"))
            .authorId(row.getLong("author_id"))
            .authorUsername(row.getString("author_username"))
            .authorName(row.getString("author_name"))
            .authorLastname(row.getString("author_lastname"))
            .title(row.getString("title"))
            .annotation(row.getString("annotation"))
            .content(row.getString("content"))
            .createdAt(row.getTimestamp("created_at"))
            .coverPath(row.getString("cover_path"))
            .build();

    @Override
    public List<News> findAll() {
        String selectSql = SELECT_ALL + ORDER_BY;
        return jdbcUtil.selectList(connection, selectSql, newsRowMapper);
    }

    @Override
    public List<News> findAll(User current) {
        List<Long> userTags = getUserTags(current);
        String selectSql = SELECT_ALL;
        if (!userTags.isEmpty()) {
            selectSql  = selectSql + LEFT_JOIN + WHERE_TAG_IN ;
        }
        selectSql = String.format(selectSql, processUserTags(userTags));
        return jdbcUtil.selectList(connection, selectSql, newsRowMapper);
    }

    @Override
    public List<News> getAllBetween(Timestamp from, Timestamp to) {
        String selectSql = SELECT_ALL + WHERE_BETWEEN + ORDER_BY;
        selectSql = String.format(selectSql, from, to);
        return jdbcUtil.selectList(connection, selectSql, newsRowMapper);
    }

    @Override
    public List<News> getAllBetweenByTags(Timestamp startOfDay, Timestamp endOfDay, User currentUser) {
        List<Long> userTags = getUserTags(currentUser);
        String selectSql = SELECT_ALL;
        if (!userTags.isEmpty()) {
            selectSql  = selectSql + LEFT_JOIN + WHERE_BETWEEN + WHERE_TAG_IN ;
        } else {
            selectSql = selectSql + WHERE_BETWEEN;
        }
        selectSql = String.format(selectSql, startOfDay, endOfDay, processUserTags(userTags));
        return jdbcUtil.selectList(connection, selectSql, newsRowMapper);
    }

    @Override
    public News get(Long id) {
        String selectSql = SELECT_ALL + WHERE_ID;
        selectSql = String.format(selectSql, id);
        return jdbcUtil.selectOne(connection, selectSql, newsRowMapper);
    }

    @Override
    public void save(News news) {
        String createSql = "insert into news (author_id, author_username, author_name, author_lastname, title, annotation, content, created_at, cover_path) values (%s, '%s', '%s', '%s', '%s', '%s', '%s', now(), '%s')";
        createSql = String.format(createSql, news.getAuthorId(), news.getAuthorUsername(), news.getAuthorName(), news.getAuthorLastname(), news.getTitle(), news.getAnnotation(), news.getContent(), news.getCoverPath());
        JdbcUtil.execute(connection, createSql);
    }

    @Override
    public void update(News news, NewsComment comment) {
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

        String updateSql = String.format("insert into news_comment values (%s, %s)", news.getId(), savedComment.getId());
        JdbcUtil.execute(connection, updateSql);
    }

    @Override
    public void update(News news, List<Tag> tagsToUpdate) {
        if (!tagsToUpdate.isEmpty()) {
            StringBuilder createSql = new StringBuilder(" insert into news_tag (news_id, tag_id) values ");

            for (Tag tag : tagsToUpdate) {
                createSql.append(String.format("(%s, %s), ", news.getId(), tag.getId()));
            }

            createSql = new StringBuilder(createSql.substring(0, createSql.length() - 2)).append(";");
            JdbcUtil.execute(connection, createSql.toString());
        }
    }

    @Override
    public News get(News news) {
        String selectSql = "select * from news where title = '%s' and annotation = '%s' and content = '%s' and author_id = %s";
        selectSql = String.format(selectSql, news.getTitle(), news.getAnnotation(), news.getContent(), news.getAuthorId());
        return jdbcUtil.selectOne(connection, selectSql, newsRowMapper);
    }

    private String processUserTags(List<Long> tagIds) {
        if (tagIds.isEmpty()) return "";
        StringBuilder sb = new StringBuilder();

        for (Long tagId : tagIds) {
            sb.append(tagId).append(", ");

        }
        String substring = sb.substring(0, sb.length() - 2);
        return substring;
    }

    private List<Long> getUserTags(User user) {
        String selectSql = "select tag_id from account_tag where account_id = %s";
        JdbcUtil<Long> additionalJdbcUtil = new JdbcUtil<>();
        selectSql = String.format(selectSql, user.getId());
        return additionalJdbcUtil.selectList(connection, selectSql, (row, number) -> row.getLong("tag_id"));
    }
}
