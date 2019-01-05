package com.apnihaveli.artiStick.mapper;

import com.apnihaveli.artiStick.model.User;
import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserMapper implements RowMapper<User> {
    private static final String USER_ID = "user_id";
    private static final String USERNAME = "username";
    private static final String HASH_PASSWORD = "hashed_password";
    private static final String FIRST_NAME = "first_name";
    private static final String LAST_NAME = "last_name";
    private static final String MAIL_ID = "mail_id";

    public User map(ResultSet resultSet, StatementContext statementContext) throws SQLException {
        return new User(
                resultSet.getString(USER_ID),
                resultSet.getString(FIRST_NAME),
                resultSet.getString(LAST_NAME),
                resultSet.getString(MAIL_ID),
                resultSet.getString(USERNAME),
                resultSet.getString(HASH_PASSWORD));
    }
}
