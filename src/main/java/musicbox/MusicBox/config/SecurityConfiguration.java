package musicbox.MusicBox.config;

import musicbox.MusicBox.model.enums.RoleEnum;
import musicbox.MusicBox.repositories.UserRepository;
import musicbox.MusicBox.services.user.CustomAuthSuccessHandler;
import musicbox.MusicBox.services.user.UserDetailsServiceImpl;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.context.DelegatingSecurityContextRepository;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.RequestAttributeSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {
    private final UserRepository userRepository;
    private final CustomAuthSuccessHandler authSuccessHandler;


    public SecurityConfiguration(UserRepository userRepository, CustomAuthSuccessHandler authSuccessHandler) {
        this.userRepository = userRepository;
        this.authSuccessHandler = authSuccessHandler;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http
            , SecurityContextRepository securityContextRepository) throws Exception {
        http
                .authorizeHttpRequests()
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
                .requestMatchers("/", "/albums/all", "/songs/all", "/artists/all",
                        "/albums/view/{id}", "/artists/view/{id}").permitAll()
                .requestMatchers("/users/register", "/users/login", "/users/login-error").anonymous()
                .requestMatchers("/users/all","*/change_role", "/songs/add/**", "/artists/add/**", "/albums/add/**"
                , "/artists/remove/**", "/albums/remove/**", "/songs/remove/**").hasRole(RoleEnum.ADMIN.name())
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .loginPage("/users/login")
                .successHandler(authSuccessHandler)
                .usernameParameter(UsernamePasswordAuthenticationFilter.SPRING_SECURITY_FORM_USERNAME_KEY)
                .passwordParameter(UsernamePasswordAuthenticationFilter.SPRING_SECURITY_FORM_PASSWORD_KEY)
                .failureForwardUrl("/users/login-error")
                .and()
                .logout().logoutUrl("/users/logout")
                .logoutSuccessUrl("/")
                .invalidateHttpSession(true).and()
                .securityContext()
                .securityContextRepository(securityContextRepository);
       http.headers().xssProtection();

        return http.build();
    }


    @Bean
    public SecurityContextRepository securityContextRepository() {
        return new DelegatingSecurityContextRepository(new RequestAttributeSecurityContextRepository()
                , new HttpSessionSecurityContextRepository());
    }
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
    @Bean
    public UserDetailsService userDetailsService(){
        return new UserDetailsServiceImpl(userRepository);
    }


}
