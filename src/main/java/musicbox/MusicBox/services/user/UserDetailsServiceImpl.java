package musicbox.MusicBox.services.user;

import musicbox.MusicBox.model.entity.UserEntity;
import musicbox.MusicBox.model.entity.UserRole;
import musicbox.MusicBox.repositories.UserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;

public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;

    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .map(this::map).orElseThrow(() -> new UsernameNotFoundException("User with username "
                + username +" doesn't exist!"));
    }
    private UserDetails map(UserEntity userEntity){
        return new User(
                userEntity.getUsername(),
                userEntity.getPassword(),
               extractAuthorities(userEntity)
        );
    }
    private List<GrantedAuthority> extractAuthorities(UserEntity userEntity){
        return userEntity
                .getRoles()
                .stream()
                .map(this::mapRole)
                .toList();
    }
    private GrantedAuthority mapRole(UserRole userRole){
        return new SimpleGrantedAuthority("ROLE_" + userRole.getRole().name());
    }
}
