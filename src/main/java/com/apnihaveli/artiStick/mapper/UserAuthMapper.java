package com.apnihaveli.artiStick.mapper;

import com.apnihaveli.artiStick.model.UserAuth;
import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserAuthMapper implements RowMapper<UserAuth> {
    private static final String USER_ID = "user_id";
    private static final String AUTH_TOKEN = "auth_token";

    public UserAuth map(ResultSet resultSet, StatementContext statementContext) throws SQLException {
        return new UserAuth(
                resultSet.getString(USER_ID),
                resultSet.getString(AUTH_TOKEN)
        );
    }
}

