package musicbox.MusicBox.model.entity;

import com.cloudinary.Cloudinary;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import musicbox.MusicBox.services.cloudinary.CloudinaryService;
import org.springframework.beans.factory.annotation.Value;

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
    @PrePersist
    @PreUpdate
    public void setDefaultImage(){

    }
    @Value("${cloudinary.cloud_name}")
    private String cloudName;

    @Value("${cloudinary.api_key}")
    private String apiKey;

    @Value("${cloudinary.api_secret}")
    private String apiSecret;

    @PreRemove
    public void preRemove() throws IOException {
        if (imageUUID != null) {
            Map<String, String> config = new HashMap<>();
            config.put("cloud_name", cloudName);
            config.put("api_key", apiKey);
            config.put("api_secret", apiSecret);
            CloudinaryService cloudinaryService = new CloudinaryService(new Cloudinary(config));
            cloudinaryService.deleteImage(imageUUID);
        }
    }
}
