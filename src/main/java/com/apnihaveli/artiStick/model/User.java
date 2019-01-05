package com.apnihaveli.artiStick.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotNull;
import java.security.Principal;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class User implements Principal{

    private String user_id;

    private String first_name;

    private String last_name;

    private String mail_id;

    private String name;

    @NotNull
    private String username;

    @NotNull
    private String password;

    @JsonIgnore
    private String hashed_password;

    public User() {
    }

    public User(String user_id, String first_name, String last_name, String mail_id, String username, String hashed_password) {
        this.user_id = user_id;
        this.username = username;
        this.hashed_password = hashed_password;
        this.first_name = first_name;
        this.last_name = last_name;
        this.mail_id = mail_id;
    }

    @Override
    public String getName() {
        return name;
    }

    public String getUserId() {
        return user_id;
    }

    public void setUserId(String user_id) {
        this.user_id = user_id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return first_name;
    }

    public void setFirstName(String first_name) {
        this.first_name = first_name;
    }

    public String getLastName() {
        return last_name;
    }

    public void setLastName(String last_name) {
        this.last_name = last_name;
    }

    public String getMailId() {
        return mail_id;
    }

    public void setMailId(String mail_id) {
        this.mail_id = mail_id;
    }

    @JsonIgnore
    public String getHashedPassword() {
        return hashed_password;
    }

    @JsonProperty
    public void setHashedPassword(String hashed_password) {
        this.hashed_password = hashed_password;
    }
}
