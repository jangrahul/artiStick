package com.apnihaveli.artiStick.mapper;

import com.apnihaveli.artiStick.model.UserImage;
import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserImageMapper implements RowMapper<UserImage> {
    private static final String USER_ID = "user_id";
    private static final String IMAGE_ID = "image_id";
    private static final String IMAGE_NAME = "image_name";
    private static final String IMAGE = "image";

    public UserImage map(ResultSet resultSet, StatementContext statementContext) throws SQLException {
        return new UserImage(
                resultSet.getString(USER_ID),
                resultSet.getString(IMAGE_ID),
                resultSet.getString(IMAGE_NAME),
                resultSet.getBytes(IMAGE));
    }
}
