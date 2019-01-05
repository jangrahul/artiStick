package com.apnihaveli.artiStick.services;

import com.apnihaveli.artiStick.dao.UserImageDao;
import com.apnihaveli.artiStick.model.UserImage;
import com.fasterxml.uuid.Generators;
import com.google.common.io.Files;
import com.rabbitmq.client.Channel;
import org.apache.commons.io.IOUtils;
import org.glassfish.jersey.media.multipart.BodyPartEntity;
import org.glassfish.jersey.media.multipart.FormDataBodyPart;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ImageService {

    private UserImageDao imagesDao;
    // TODO: where to keep constants like these
    private String[] VALID_EXTENSIONS = new String[] {"jpeg", "jpg", "png"};
    private Channel amqpChannel;

    public ImageService(UserImageDao imagesDao) {
        this.imagesDao = imagesDao;
    }

    public ImageService(UserImageDao imagesDao, Channel amqpChannel) {
        this.imagesDao = imagesDao;
        this.amqpChannel = amqpChannel;
    }

    public List<UserImage> getImages(){
        return imagesDao.getAllImages();
    }

    public void createImages(
            List<FormDataBodyPart> imageParts,
            FormDataContentDisposition fileDispositions,
            String userId) {

        List<UserImage> images = new ArrayList<UserImage>();
        ArrayList<String> imageIds = new ArrayList<String>();

        // TODO: is this pattern correct (making new UserImage and then setting properties)
        // TODO: how to handle try catches
        for(int i=0; i<imageParts.size(); i++) {
            BodyPartEntity imageEntity = (BodyPartEntity) imageParts.get(i).getEntity();
            String imgName = imageParts.get(i).getContentDisposition().getFileName();
            StringBuilder imageName = new StringBuilder(Files.getNameWithoutExtension(imgName));
            String extension = imgName.substring(imgName.lastIndexOf('.')+1);

            if(!Arrays.stream(VALID_EXTENSIONS).anyMatch( x -> x.equals(extension))) {
                continue;
            }

            if(imagesDao.findByUserAndImage(userId, imgName) != null) {
                continue;
            }

            final String imageId = Generators.timeBasedGenerator().generate().toString();
            UserImage image = new UserImage();
            image.setImageId(imageId);
            image.setImageName(imageName + "." + extension);
            image.setUserId(userId);
            try{
                image.setImage(IOUtils.toByteArray(imageEntity.getInputStream()));
            }catch (IOException e){
            }
            images.add(image);

            System.out.println("Added: " + imageId);
            imageIds.add(imageId);
        }

        imagesDao.createImages(images);
        try{
            QueueService.addToQueueMultiple(amqpChannel, QueueNames.DB_TO_FLICKR.toString(), imageIds);
        }catch (IOException e){}
    }

    public UserImage getImageById(String imageId) {
        return imagesDao.getImageById(imageId);
    }
}
