package com.apnihaveli.artiStick.services;

import com.apnihaveli.artiStick.dao.UserDao;
import com.apnihaveli.artiStick.model.Login;
import com.apnihaveli.artiStick.util.hashUtil;
import com.apnihaveli.artiStick.model.User;
import com.apnihaveli.artiStick.services.representation.UserRep;
import com.fasterxml.uuid.Generators;
import com.flickr4java.flickr.Flickr;
import com.flickr4java.flickr.FlickrException;
import com.flickr4java.flickr.REST;
import com.flickr4java.flickr.RequestContext;
import com.flickr4java.flickr.auth.Auth;
import com.flickr4java.flickr.auth.AuthInterface;
import com.flickr4java.flickr.auth.Permission;
import org.scribe.model.Token;
import org.scribe.model.Verifier;

import java.util.List;
import java.util.Scanner;

public class UserService {

    private UserDao userDao;

    private UserAuthService userAuthService;

    public UserService(UserDao userDao, UserAuthService userAuthService) {
        this.userDao = userDao;
        this.userAuthService = userAuthService;
    }

    public List<User> getUsers() {
        return userDao.getUsers();
    }

    public UserRep createUser(User user) {
        String message = null;
        String token = null;
        boolean success = false;
        User createdUser = null;

        if(userDao.findByUserName(user.getUsername()) != null){
            message = "Username already exists";
        }else{
            user.setUserId(Generators.timeBasedGenerator().generate().toString());

            user.setHashedPassword(hashUtil.generateHash(user.getPassword()));
            createdUser = userDao.createUser(user);
            token = userAuthService.createUserAuth(user);
            success = true;
        }
        return new UserRep(success, message, token, createdUser);
    }

    public UserRep login(Login credentials) {
        String message = null;
        String token = null;
        boolean success = false;
        User user = userDao.findByUserName(credentials.getUsername());

        if(user != null){
            String incomingHashedPassword = hashUtil.generateHash(credentials.getPassword());
            if (incomingHashedPassword.equals(user.getHashedPassword())){
                message = "success";
                success = true;
                token = userAuthService.findByUserId(user.getUserId()).getAuthToken();
            } else {
                message = "Invalid Credentials";
                user = null;
            }
        }else{
            message = "Invalid Credentials";
        }
        return new UserRep(success, message, token, user);
    }

    public User findByUserId(String userId) {
        return userDao.findByUserId(userId);
    }

    public void getAdminToken(Flickr flickr) throws FlickrException {

        AuthInterface authInterface = flickr.getAuthInterface();
        Token accessToken = authInterface.getRequestToken();

        // Try with DELETE permission. At least need write permission for upload and add-to-set.
        String url = authInterface.getAuthorizationUrl(accessToken, Permission.WRITE);
        System.out.println("Follow this URL to authorise yourself on Flickr");
        System.out.println(url);
        System.out.println("Paste in the token it gives you:");
        System.out.print(">>");

        Scanner scanner = new Scanner(System.in);
        String tokenKey = scanner.nextLine();

        Token requestToken = authInterface.getAccessToken(accessToken, new Verifier(tokenKey));

        Auth auth = authInterface.checkToken(requestToken);
        RequestContext.getRequestContext().setAuth(auth);
        scanner.close();

        System.out.println(" AuthToken: " + auth.getToken() + " tokenSecret: " + auth.getTokenSecret());
    }
}
