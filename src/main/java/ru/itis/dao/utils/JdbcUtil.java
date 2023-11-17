package ru.itis.dao.utils;

import org.postgresql.Driver;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class JdbcUtil<T> {
    private static Connection conn = null;
    private static final String URL = "jdbc:postgresql://localhost:5431/postgres";
    private static final String USERNAME = "postgres";
    private static final String PASSWORD = "password";

    public static Connection getConnection() {
        if (conn == null) {
            try {
                DriverManager.registerDriver(new Driver());
                conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            } catch (SQLException e) {
                throw new IllegalStateException(e);
            }
        }
        return conn;
    }

    public static void execute(Connection connection, String sql) {
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate(sql);
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    public T selectOne(Connection connection, String sql, RowMapper<T> rowMapper) {
        try (Statement statement = connection.createStatement()) {
            statement.execute(sql);
            ResultSet resultSet = statement.getResultSet();

            if (resultSet.next()) {
                return rowMapper.mapRow(resultSet, resultSet.getRow());
            } else {
                return null;
            }
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    public List<T> selectList(Connection connection, String sql, RowMapper<T> rowMapper) {
        try (Statement statement = connection.createStatement()) {
            List<T> result = new ArrayList<>();
            statement.execute(sql);
            ResultSet resultSet = statement.getResultSet();

            while (resultSet.next()) {
                result.add(rowMapper.mapRow(resultSet, resultSet.getRow()));
            }

            return result;
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
    }
}
