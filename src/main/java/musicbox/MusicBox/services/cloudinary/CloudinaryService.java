package musicbox.MusicBox.services.cloudinary;

import com.cloudinary.Cloudinary;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class CloudinaryService {

    private final Cloudinary cloudinary;

    public CloudinaryService(Cloudinary cloudinary) {
        this.cloudinary = cloudinary;
    }


    public Map<String, String> uploadImage(MultipartFile imageFile) throws IOException {

        if (imageFile == null || imageFile.isEmpty()) {
            return new HashMap<>();
        }

        File image = File.createTempFile("temp", null);
        imageFile.transferTo(image);
        Map<String, String> config = new HashMap<>();
        config.put("folder", "images");
        config.put("public_id", UUID.randomUUID().toString());
        config.put("resource_type", "image");
        Map<?, ?> result = cloudinary.uploader().upload(image, config);
        Map<String, String> response = new HashMap<>();
        response.put("secure_url", result.get("secure_url").toString());
        response.put("public_id", result.get("public_id").toString());
        return response;
    }

    public void deleteImage(String publicId) throws IOException {
        if (publicId != null) {
            this.cloudinary.uploader().destroy(publicId, null);
        }
    }

    public boolean isImage(MultipartFile file) {
        if (file.getContentType() != null && file.getContentType().startsWith("image/")) {
            try {
                ImageIO.read(file.getInputStream());
                return true;

            } catch (Exception e) {
                return false;
            }
        } else {
            return false;
        }
    }


}

