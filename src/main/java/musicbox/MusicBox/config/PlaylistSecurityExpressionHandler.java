package musicbox.MusicBox.config;

import musicbox.MusicBox.services.playlist.PlaylistService;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionOperations;
import org.springframework.security.authentication.AuthenticationTrustResolverImpl;
import org.springframework.security.core.Authentication;

public class PlaylistSecurityExpressionHandler extends DefaultMethodSecurityExpressionHandler {

    private final PlaylistService playlistService;

    public PlaylistSecurityExpressionHandler(PlaylistService playlistService) {
        this.playlistService = playlistService;
    }

    @Override
    protected MethodSecurityExpressionOperations createSecurityExpressionRoot(Authentication authentication,
                                                                              MethodInvocation invocation) {

        OwnerSecurityExpressionRoot root = new OwnerSecurityExpressionRoot(authentication, playlistService);

        root.setPermissionEvaluator(getPermissionEvaluator());
        root.setTrustResolver(new AuthenticationTrustResolverImpl());
        root.setRoleHierarchy(getRoleHierarchy());

        return root;
    }
}
