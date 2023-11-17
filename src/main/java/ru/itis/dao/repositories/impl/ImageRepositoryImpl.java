package ru.itis.dao.repositories.impl;

import ru.itis.dao.entities.Image;
import ru.itis.dao.repositories.ImageRepository;
import ru.itis.dao.utils.JdbcUtil;
import ru.itis.dao.utils.RowMapper;

import javax.servlet.http.Part;
import java.sql.Connection;
import java.util.List;

public class ImageRepositoryImpl implements ImageRepository {

    private final Connection connection;
    private final JdbcUtil<Image> jdbcUtil;
    private static final String DIRECTORY_PATH = "images/";

    public ImageRepositoryImpl(Connection connection) {
        this.connection = connection;
        this.jdbcUtil = new JdbcUtil<>();
    }

    private final RowMapper<Image> imageRowMapper = (row, number) -> Image.builder()
            .id(row.getLong("id"))
            .fileName(row.getString("filename"))
            .originalFileName(row.getString("original_filename"))
            .filePath(row.getString("file_path"))
            .contentType(row.getString("content_type"))
            .size(row.getInt("size"))
            .build();

    @Override
    public Image find(String fileName) {
        String selectSql = String.format("select * from image where file_path = '%s'", DIRECTORY_PATH + fileName);
        return jdbcUtil.selectOne(connection, selectSql, imageRowMapper);
    }

    @Override
    public void create(Part file) {
        String createSql = "insert into image (filename, original_filename, file_path, content_type, size) values ('%s', '%s', '%s', '%s', %s)";
        createSql = String.format(createSql, file.getName(), file.getSubmittedFileName(), DIRECTORY_PATH + file.getSubmittedFileName(), file.getContentType(), file.getSize());
        JdbcUtil.execute(connection, createSql);
    }

    @Override
    public List<Image> findAll() {
        String selectSql = "select * from image order by id desc";
        return jdbcUtil.selectList(connection, selectSql, imageRowMapper);
    }

    @Override
    public void create(Image image) {
        String createSql = "insert into image (filename, original_filename, file_path, content_type, size) values (%s, %s, %s, '%s', %s)";
        createSql = String.format(createSql, image.getFileName(), image.getOriginalFileName(), DIRECTORY_PATH + image.getFileName(), image.getContentType(), image.getSize());
        JdbcUtil.execute(connection, createSql);
    }
}
