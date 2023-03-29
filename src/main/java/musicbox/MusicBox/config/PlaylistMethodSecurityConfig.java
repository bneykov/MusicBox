package musicbox.MusicBox.config;


import musicbox.MusicBox.services.playlist.PlaylistService;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class PlaylistMethodSecurityConfig extends GlobalMethodSecurityConfiguration {

    private final PlaylistService playlistService;

    public PlaylistMethodSecurityConfig(PlaylistService playlistService) {
        this.playlistService = playlistService;
    }

    @Override
    protected MethodSecurityExpressionHandler createExpressionHandler() {
        return new PlaylistSecurityExpressionHandler(playlistService);
    }
}
