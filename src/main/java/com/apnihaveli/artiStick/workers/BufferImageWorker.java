package com.apnihaveli.artiStick.workers;

import com.apnihaveli.artiStick.services.ImageService;

public class BufferImageWorker implements WorkerInterface{

    // TODO: Is this pattern right, ImageService is coming all the way from ArtiStickApplication.
    private ImageService imageService;
    private String imageId;

    public BufferImageWorker(ImageService imageService, String imageId) {
        this.imageService = imageService;
        this.imageId = imageId;
    }

    @Override
    public void run(){

    }
}