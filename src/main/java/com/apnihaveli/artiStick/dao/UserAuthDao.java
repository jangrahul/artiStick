package com.apnihaveli.artiStick.dao;

import com.apnihaveli.artiStick.mapper.UserAuthMapper;
import com.apnihaveli.artiStick.model.User;
import com.apnihaveli.artiStick.model.UserAuth;
import org.jdbi.v3.sqlobject.config.RegisterRowMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.SqlQuery;

import java.util.List;

@RegisterRowMapper(UserAuthMapper.class)
public interface UserAuthDao {

    @SqlQuery("select * from user_authentications;")
    public List<UserAuth> getUsers();

    @SqlQuery("insert into user_authentications(user_id, auth_token) values(:userId, :authToken) returning *;")
    public UserAuth createUserAuth(@BindBean final UserAuth userAuth);

    @SqlQuery("select * from user_authentications where auth_token = :authToken;")
    public UserAuth findByToken(@Bind("authToken") final String token);

    @SqlQuery("select * from user_authentications where user_id = :userId;")
    public UserAuth findByUserId(@Bind("userId") final String userId);

}
