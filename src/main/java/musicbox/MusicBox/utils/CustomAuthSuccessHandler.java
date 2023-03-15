package musicbox.MusicBox.utils;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import musicbox.MusicBox.model.entity.UserEntity;
import musicbox.MusicBox.repositories.UserRepository;
import musicbox.MusicBox.utils.events.UserLoggedInEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class CustomAuthSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private final UserRepository userRepository;
    private final ApplicationEventPublisher applicationEventPublisher;

    @Autowired
    public CustomAuthSuccessHandler(UserRepository userRepository, ApplicationEventPublisher applicationEventPublisher) {
        this.userRepository = userRepository;
        this.applicationEventPublisher = applicationEventPublisher;
    }


    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        response.sendRedirect("/home");
        super.onAuthenticationSuccess(request, response, authentication);
        UserEntity user = this.userRepository.findByUsername(authentication.getName()).orElse(null);
        applicationEventPublisher.publishEvent(new UserLoggedInEvent(this, user));

    }
}
