package musicbox.MusicBox.config;


import musicbox.MusicBox.services.playlist.PlaylistService;
import org.springframework.security.access.expression.SecurityExpressionRoot;
import org.springframework.security.access.expression.method.MethodSecurityExpressionOperations;
import org.springframework.security.core.Authentication;


public class OwnerSecurityExpressionRoot
        extends SecurityExpressionRoot
        implements MethodSecurityExpressionOperations {

    private final Authentication authentication;
    private final PlaylistService playlistService;
    private Object filterObject;
    private Object returnObject;

    public OwnerSecurityExpressionRoot(Authentication authentication, PlaylistService playlistService) {
        super(authentication);
        this.authentication = authentication;
        this.playlistService = playlistService;
    }

    public boolean isOwner(Long id) {
        if (authentication.getPrincipal() == null) {
            return false;
        }

        return playlistService.isOwner(authentication.getName(), id);
    }


    @Override
    public void setFilterObject(Object filterObject) {
        this.filterObject = filterObject;
    }

    @Override
    public Object getFilterObject() {
        return filterObject;
    }

    @Override
    public void setReturnObject(Object returnObject) {
        this.returnObject = returnObject;
    }

    @Override
    public Object getReturnObject() {
        return this.returnObject;
    }

    @Override
    public Object getThis() {
        return this;
    }
}
