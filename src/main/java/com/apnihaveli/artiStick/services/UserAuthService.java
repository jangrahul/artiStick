package com.apnihaveli.artiStick.services;

import com.apnihaveli.artiStick.dao.UserAuthDao;
import com.apnihaveli.artiStick.model.User;
import com.apnihaveli.artiStick.model.UserAuth;
import com.apnihaveli.artiStick.util.jwt;

public class UserAuthService {

    private UserAuthDao userAuthDao;

    public UserAuthService(UserAuthDao userAuthDao) {
        this.userAuthDao = userAuthDao;
    }

    public String createUserAuth(User user) {
        String token = jwt.generateJwt(user.getUserId());
        userAuthDao.createUserAuth(new UserAuth(user.getUserId(), token));
        return token;
    }

    public UserAuth findByUserId(String userId) {
        return userAuthDao.findByUserId(userId);
    }

    public UserAuth findByToken(String token) {
        return userAuthDao.findByToken(token);
    }
}
