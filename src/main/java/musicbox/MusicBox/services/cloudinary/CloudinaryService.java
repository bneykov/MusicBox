package musicbox.MusicBox.services.cloudinary;

import com.cloudinary.Cloudinary;
import org.springframework.stereotype.Service;

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


    public Map<String, String> uploadImage(File imageFile) throws IOException {
        Map<String, String> params = new HashMap<>();
        params.put("folder", "images");
        params.put("public_id", UUID.randomUUID().toString());
        params.put("resource_type", "image");

        Map<?, ?> result = cloudinary.uploader().upload(imageFile, params);
        Map<String, String> response = new HashMap<>();
        response.put("secure_url", result.get("secure_url").toString());
        response.put("public", result.get("public_id").toString());
        return response;
    }
    public void deleteImage(String publicId) throws IOException {
        this.cloudinary.uploader().destroy(publicId, null);
    }
}
