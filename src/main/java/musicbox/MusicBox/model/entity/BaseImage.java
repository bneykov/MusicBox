package musicbox.MusicBox.model.entity;

import com.cloudinary.Cloudinary;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.SuperBuilder;
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
public abstract class BaseImage extends BaseEntity {

    @NotNull
    @Column(name = "name")
    private String name;


    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "image_uuid")
    private String imageUUID;
    @PrePersist
    public void setDefaultImage(){

    }

    @PreRemove
    public void preRemove() throws IOException {
        if (imageUUID != null) {
            Map<String, String> config = new HashMap<>();
            config.put("cloud_name", "bneikov");
            config.put("api_key", "133183588698468");
            config.put("api_secret", "qAeWm3WbSFi2eueRLC5GaabzjYs");
            CloudinaryService cloudinaryService = new CloudinaryService(new Cloudinary(config));
            cloudinaryService.deleteImage(imageUUID);
        }
    }
}
