package com.apnihaveli.artiStick.resources;

import com.apnihaveli.artiStick.apiResponse.Representation;
import com.apnihaveli.artiStick.model.Login;
import com.apnihaveli.artiStick.model.User;
import com.apnihaveli.artiStick.services.UserService;
import com.apnihaveli.artiStick.services.representation.UserRep;
import com.codahale.metrics.annotation.Timed;
import com.flickr4java.flickr.Flickr;
import com.flickr4java.flickr.FlickrException;
import io.dropwizard.auth.Auth;
import org.eclipse.jetty.http.HttpStatus;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/api/v1/user")
@Produces(MediaType.APPLICATION_JSON)
public class UserResource {
    private final UserService userService;

    private final Flickr adminFlickr;

    public UserResource(UserService userService, Flickr adminFlickr) {
        this.userService = userService;
        this.adminFlickr = adminFlickr;
    }

    @GET
    @Timed
    public Representation<List<User>> getUsers(@Auth User user) {
        return new Representation<>(HttpStatus.OK_200, userService.getUsers());
    }

    @POST
    @Path("/create")
    @Timed
    public Representation<UserRep> createUser(@NotNull @Valid final User user) {
        UserRep data = userService.createUser(user);
        int status = data.getSuccess() ? HttpStatus.OK_200 : HttpStatus.NOT_ACCEPTABLE_406 ;
        return new Representation<UserRep>(status, data);
    }

    @POST
    @Path("/login")
    @Timed
    public Representation<UserRep> loginUser(@NotNull @Valid Login credentials) {
        UserRep data = userService.login(credentials);
        int status = data.getSuccess() ? HttpStatus.OK_200 : HttpStatus.NOT_ACCEPTABLE_406 ;
        return new Representation<UserRep>(status, data);
    }

    @GET
    @Path("/admin_token")
    @Timed
    public int getAdminToken(@Auth User user) {
        try {
            userService.getAdminToken(adminFlickr);
        }catch (FlickrException e) {System.out.println(e);}
        return HttpStatus.OK_200;
    }

}
