package musicbox.MusicBox.model.entity;

import com.cloudinary.Cloudinary;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import musicbox.MusicBox.constants.CloudinaryConfig;
import musicbox.MusicBox.services.cloudinary.CloudinaryService;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@MappedSuperclass
public abstract class BaseImageEntity extends BaseEntity {

    @NotNull
    @Column(name = "name")
    private String name;


    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "image_uuid")
    private String imageUUID;

    //Set default image based on the entity type
    @PrePersist
    @PreUpdate
    public void prePersist() {

    }
    //Remove image from the cloud if the entity being removed has image that's not set by default
    @PreRemove
    public void preRemove() throws IOException {
        if (imageUUID != null) {
            Map<String, String> config = new HashMap<>();
            config.put("cloud_name", CloudinaryConfig.CLOUDINARY_CLOUD_NAME);
            config.put("api_key", CloudinaryConfig.CLOUDINARY_API_KEY);
            config.put("api_secret", CloudinaryConfig.CLOUDINARY_API_SECRET);
            CloudinaryService cloudinaryService = new CloudinaryService(new Cloudinary(config));
            cloudinaryService.deleteImage(imageUUID);
        }
    }
}
