package musicbox.MusicBox.model;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;


public class CustomUserDetails extends User {
    private Long id;

    public Long getId() {
        return id;
    }

    public CustomUserDetails setId(Long id) {
        this.id = id;
        return this;
    }

    private String name;
    private String email;

    private String imageUrl;

    public String getImageUrl() {
        return imageUrl;
    }

    public CustomUserDetails setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
        return this;
    }

    public String getName() {
        return name;
    }

    public CustomUserDetails setName(String name) {
        this.name = name;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public CustomUserDetails setEmail(String email) {
        this.email = email;
        return this;
    }

    public CustomUserDetails(String username, String password, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);
    }
}
