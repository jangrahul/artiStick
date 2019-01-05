package com.apnihaveli.artiStick.dao;

import com.apnihaveli.artiStick.mapper.UserImageMapper;
import com.apnihaveli.artiStick.model.UserImage;
import org.jdbi.v3.sqlobject.config.RegisterRowMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.SqlBatch;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import java.util.List;

@RegisterRowMapper(UserImageMapper.class)
public interface UserImageDao {

    @SqlBatch("insert into user_images(user_id, image_id, image_name, image) values(:userId, :imageId, :imageName, :image);")
    public void createImages(@BindBean final List<UserImage> userImages);

    @SqlQuery("select * from user_images")
    public List<UserImage> getAllImages();

    @SqlQuery("select * from user_images where image_name = :imageName and user_id = :userId;")
    public UserImage findByUserAndImage(@Bind("userId") final String userId, @Bind("imageName") final String imageName);

    @SqlQuery("select * from user_images where image_id = :imageId;")
    public UserImage getImageById(@Bind("imageId") final String imageId);

}
