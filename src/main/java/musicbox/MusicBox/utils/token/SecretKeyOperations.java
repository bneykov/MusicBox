package musicbox.MusicBox.utils.token;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

public class SecretKeyOperations {


    public static String encrypt(SecretKey secretKey) {
        return Base64.getEncoder().encodeToString(secretKey.getEncoded());
    }

    public static SecretKey decrypt(String encryptedSecretKey) {
        byte[] decodedKey = Base64.getDecoder().decode(encryptedSecretKey);
        return new SecretKeySpec(decodedKey, 0, decodedKey.length, "HmacSHA256");
    }
}

