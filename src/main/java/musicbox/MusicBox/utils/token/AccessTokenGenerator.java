package musicbox.MusicBox.utils.token;

import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import javax.crypto.SecretKey;
import java.util.Date;

public class AccessTokenGenerator {
    private static final long EXPIRATION_TIME = 24 * 60 * 60 * 1000;

    public static String generateAccessToken(Long playlistId, String secretKey) {
        Date now = new Date();
        Date expiration = new Date(now.getTime() + EXPIRATION_TIME);
        SecretKey decryptedKey = SecretKeyOperations.decrypt(secretKey);
        JwtBuilder builder = Jwts.builder().setSubject(playlistId.toString())
                .setIssuedAt(now)
                .setExpiration(expiration)
                .signWith(decryptedKey, SignatureAlgorithm.HS256);
        return builder.compact();
    }

}
