package com.apnihaveli.artiStick.consumers;

import com.apnihaveli.artiStick.services.ImageService;
import com.apnihaveli.artiStick.tasks.BufferImageUploadTask;
import com.flickr4java.flickr.Flickr;
import com.flickr4java.flickr.auth.Auth;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ImageBufferConsumer {

    private ImageService imageService;

    private final ExecutorService executor = Executors.newFixedThreadPool(2);

    public ImageBufferConsumer(ImageService imageService) {
        this.imageService = imageService;
    }

    public void consume(String imageId, Flickr adminFlickr, Auth flickrAdminAuth) {
        if(imageId != null){

            BufferImageUploadTask task = new BufferImageUploadTask(imageService, imageId, adminFlickr, flickrAdminAuth);

            executor.execute(task);
        }
    }
}
