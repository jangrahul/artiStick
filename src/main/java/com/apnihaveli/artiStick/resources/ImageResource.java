package com.apnihaveli.artiStick.resources;

import com.apnihaveli.artiStick.model.User;
import com.apnihaveli.artiStick.model.UserImage;
import com.apnihaveli.artiStick.services.ImageService;
import io.dropwizard.auth.Auth;
import org.glassfish.jersey.media.multipart.*;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.List;

@Path("/api/v1/images")
@Produces(MediaType.APPLICATION_JSON)
public class ImageResource {

    private final ImageService imageService;

    public ImageResource(ImageService imageService) {
        this.imageService = imageService;
    }

    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response uploadImages(
            @Auth User user,
            @FormDataParam("files") List<FormDataBodyPart> imageParts,
            @FormDataParam("files") FormDataContentDisposition fileDispositions) throws IOException {

        imageService.createImages(imageParts, fileDispositions, user.getUserId());

        return Response.ok().build();
    }

    @GET
    public List<UserImage> getImages() {
        return imageService.getImages();
    }
}
