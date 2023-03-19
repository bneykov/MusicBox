package musicbox.MusicBox.services.user;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;


public class CustomUserDetails implements UserDetails {
    private final Long id;
    private final String name;
    private final String username;
    private final String email;
    private final String password;

    private final Collection<GrantedAuthority> authorities;

    private final String imageUrl;
    private final String imageUUID;
    private final String created;
    private final String modified;
    private final String lastLoggedIn;




    public CustomUserDetails(Long id, String name, String username, String email,
                             String password, Collection<GrantedAuthority> authorities,
                             String imageUrl, String imageUUID, String created,
                             String modified, String lastLoggedIn) {
        this.id = id;
        this.name = name;
        this.username = username;
        this.email = email;
        this.password = password;
        this.authorities = authorities;
        this.imageUrl = imageUrl;
        this.imageUUID = imageUUID;
        this.created = created;
        this.modified = modified;
        this.lastLoggedIn = lastLoggedIn;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getImageUUID() {
        return imageUUID;
    }

    public String getCreated() {
        return created;
    }

    public String getModified() {
        return modified;
    }

    public String getLastLoggedIn() {
        return lastLoggedIn;
    }
}
