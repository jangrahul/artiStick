package com.apnihaveli.artiStick.model;

public class UserImage {
    private String userId;

    private String imageId;

    private String imageName;

    private byte[] image;

    public UserImage() {
    }

    public UserImage(String userId, String imageId, String imageName, byte[] image) {
        this.userId = userId;
        this.imageId = imageId;
        this.imageName = imageName;
        this.image = image;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getImageId() {
        return imageId;
    }

    public void setImageId(String imageId) {
        this.imageId = imageId;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }
}
