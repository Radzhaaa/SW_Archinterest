package ru.itis.dao.utils;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface RowMapper<T> {
    T mapRow(ResultSet row, int number) throws SQLException;
}
