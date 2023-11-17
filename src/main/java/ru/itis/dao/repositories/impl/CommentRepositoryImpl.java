package ru.itis.dao.repositories.impl;

import ru.itis.dao.entities.News;
import ru.itis.dao.entities.NewsComment;
import ru.itis.dao.entities.Project;
import ru.itis.dao.entities.ProjectComment;
import ru.itis.dao.repositories.CommentRepository;
import ru.itis.dao.utils.JdbcUtil;
import ru.itis.dao.utils.RowMapper;

import java.sql.Connection;
import java.util.List;

public class CommentRepositoryImpl implements CommentRepository {

    private final Connection connection;
    private final JdbcUtil<NewsComment> newsCommentJdbcUtil;
    private final JdbcUtil<ProjectComment> projectCommentJdbcUtil;

    private static final String SQL_GET = "select c.*, a.username";
    private static final String FROM =" from comment c";
    private static final String SQL_JOIN_PIN = " left join project_comment pc on pc.comment_id = c.id";
    private static final String SQL_JOIN_NEWS = " left join news_comment nc on nc.comment_id = c.id";
    private static final String SQL_JOIN_USER = " left join account a on a.id = c.author_id";
    private static final String SQL_ORDER_BY = " order by created_at";
    private static final String SQL_GET_BY_PROJECT_ID = SQL_GET + ", pc.project_id" + FROM + SQL_JOIN_USER + SQL_JOIN_PIN + " where pc.project_id = %s" + SQL_ORDER_BY;
    private static final String SQL_GET_BY_NEWS_ID = SQL_GET + ", nc.news_id" + FROM + SQL_JOIN_USER + SQL_JOIN_NEWS + " where nc.news_id = %s" + SQL_ORDER_BY;
    private static final String LIKE = "update comment set likes = likes + 1 where id = %s";
    private static final String DISLIKE = "update comment set dislikes = dislikes + 1 where id = %s";

    public CommentRepositoryImpl(Connection connection) {
        this.connection = connection;
        this.newsCommentJdbcUtil = new JdbcUtil<>();
        this.projectCommentJdbcUtil = new JdbcUtil<>();
    }

    private final RowMapper<NewsComment> newsCommentRowMapper = (row, number) -> NewsComment.builder()
            .id(row.getLong("id"))
            .newsId(row.getLong("news_id"))
            .authorId(row.getLong("author_id"))
            .authorUsername(row.getString("username"))
            .avatarPath(row.getString("avatar_path"))
            .content(row.getString("content"))
            .createdAt(row.getTimestamp("created_at"))
            .likes(row.getInt("likes"))
            .dislikes(row.getInt("dislikes"))
            .build();

    private final RowMapper<ProjectComment> projectCommentRowMapper = (row, number) -> ProjectComment.builder()
            .id(row.getLong("id"))
            .projectId(row.getLong("project_id"))
            .authorId(row.getLong("author_id"))
            .authorUsername(row.getString("username"))
            .avatarPath(row.getString("avatar_path"))
            .content(row.getString("content"))
            .createdAt(row.getTimestamp("created_at"))
            .likes(row.getInt("likes"))
            .dislikes(row.getInt("dislikes"))
            .build();



    @Override
    public List<NewsComment> findAll(News news) {
        String selectSql = String.format(SQL_GET_BY_NEWS_ID, news.getId());
        return newsCommentJdbcUtil.selectList(connection, selectSql, newsCommentRowMapper);
    }

    @Override
    public List<ProjectComment> findAll(Project project) {
        String selectSql = String.format(SQL_GET_BY_PROJECT_ID, project.getId());
        return projectCommentJdbcUtil.selectList(connection, selectSql, projectCommentRowMapper);
    }

    @Override
    public void like(Long commentId) {
        String updateSQL = String.format(LIKE, commentId);
        JdbcUtil.execute(connection, updateSQL);
    }

    @Override
    public void dislike(Long commentId) {
        String updateSQL = String.format(DISLIKE, commentId);
        JdbcUtil.execute(connection, updateSQL);
    }
}
