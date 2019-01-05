package com.apnihaveli.artiStick.dao;

import com.apnihaveli.artiStick.mapper.UserMapper;
import com.apnihaveli.artiStick.model.User;
import org.jdbi.v3.sqlobject.config.RegisterRowMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import java.util.List;

@RegisterRowMapper(UserMapper.class)
public interface UserDao {

    @SqlQuery("select * from users;")
    public List<User> getUsers();

    @SqlQuery("select * from users where username = :username;")
    public User findByUserName(@Bind("username") final String username);

    @SqlQuery("select * from users where user_id = :userId;")
    public User findByUserId(@Bind("userId") final String userId);

    @SqlQuery("insert into users(user_id, username, hashed_password, first_name, last_name, mail_id) values(:userId, :username, :hashedPassword, :firstName, :lastName, :mailId) returning *;")
    public User createUser(@BindBean final User user);
}
