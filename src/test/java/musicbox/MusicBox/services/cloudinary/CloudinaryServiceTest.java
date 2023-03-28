package musicbox.MusicBox.services.cloudinary;

import com.cloudinary.Cloudinary;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
public class CloudinaryServiceTest {
    private CloudinaryService cloudinaryService;
    @Mock
    private Cloudinary cloudinary;



    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        cloudinaryService = new CloudinaryService(cloudinary);

    }

    @Test
    @DisplayName("testUploadImage should return empty Map if the image file provided is null or empty")
    void testUploadImageWithNullOrEmptyImageFile() throws IOException {
        MockMultipartFile file = new MockMultipartFile("test.abv", InputStream.nullInputStream());
        assertTrue(cloudinaryService.uploadImage(null).isEmpty());
        assertTrue(cloudinaryService.uploadImage(file).isEmpty());
    }
    @Test
    void testIsImageWithValidImage() throws IOException {

        byte[] data = new byte[] {1, 2, 3, 4};
        InputStream stream = new ByteArrayInputStream(data);
        MockMultipartFile file = new MockMultipartFile("test.png", "", "image/png", stream);
        assertTrue(cloudinaryService.isImage(file));
    }
    @Test
    void testIsImageWithInvalidImage() throws IOException {

        byte[] data = new byte[] {1, 2, 3, 4};
        InputStream stream = new ByteArrayInputStream(data);
        MockMultipartFile file = new MockMultipartFile("test.png", "", "notImage/", stream);
        assertFalse(cloudinaryService.isImage(file));
    }
}
