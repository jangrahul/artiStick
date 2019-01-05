package com.apnihaveli.artiStick.auth;

import com.apnihaveli.artiStick.model.User;
import com.apnihaveli.artiStick.model.UserAuth;
import com.apnihaveli.artiStick.services.UserAuthService;
import com.apnihaveli.artiStick.services.UserService;
import io.dropwizard.auth.AuthenticationException;
import io.dropwizard.auth.Authenticator;

import java.util.Optional;

public class UserAuthenticator implements Authenticator<String, User> {

    private UserService userService;

    private UserAuthService userAuthService;

    public UserAuthenticator(UserService userService, UserAuthService userAuthService) {
        this.userService = userService;
        this.userAuthService = userAuthService;
    }

    @Override
    public Optional<User> authenticate(String authToken) throws AuthenticationException {
        UserAuth ua = userAuthService.findByToken(authToken);
        if(ua != null) {
            User user = userService.findByUserId(ua.getUserId());
            if(user != null)
                return Optional.of(user);
        }
        return Optional.empty();
    }
}
