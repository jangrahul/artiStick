package com.apnihaveli.artiStick.tasks;

import com.apnihaveli.artiStick.model.UserImage;
import com.apnihaveli.artiStick.services.ImageService;
import com.flickr4java.flickr.Flickr;
import com.flickr4java.flickr.RequestContext;
import com.flickr4java.flickr.auth.Auth;
import com.flickr4java.flickr.photos.PhotosInterface;
import com.flickr4java.flickr.photos.Size;
import com.flickr4java.flickr.uploader.UploadMetaData;
import com.flickr4java.flickr.uploader.Uploader;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Collection;

public class BufferImageUploadTask implements Runnable {

    private ImageService imageService;

    private String imageId;

    private Flickr flickr;

    private Auth flickrAdminAuth;

    public BufferImageUploadTask(ImageService imageService, String imageId, Flickr flickr, Auth flickrAdminAuth) {
        this.imageService = imageService;
        this.imageId = imageId;
        this.flickr = flickr;
        this.flickrAdminAuth = flickrAdminAuth;
    }

    @Override
    public void run() {
        UserImage image = imageService.getImageById(imageId);
        byte[] imageBytes = image.getImage();
        if(imageBytes == null)
            return;

        String imageName = image.getImageName();
        String prefix = imageName.substring(0, imageName.lastIndexOf('.'));
        String suffix = imageName.substring(imageName.lastIndexOf('.')+1);

        RequestContext.getRequestContext().setAuth(flickrAdminAuth);

        UploadMetaData metaData = new UploadMetaData();
        metaData.setTitle(prefix);
        metaData.setFilename(imageName);

        Uploader uploader = flickr.getUploader();
        PhotosInterface photosInterface = flickr.getPhotosInterface();
        try{
            File file = File.createTempFile(prefix, "."+suffix, null);
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(imageBytes);
            String photoId = uploader.upload(file, metaData);
            System.out.println("File uploaded: "+ photoId + " name" + imageName);
            file.delete();

            Collection<Size> sizes = photosInterface.getSizes(photoId);
        }catch (Exception e){
            System.out.println(e);
        }



    }
}
