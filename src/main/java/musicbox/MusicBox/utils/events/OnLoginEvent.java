package musicbox.MusicBox.utils.events;

import lombok.Getter;
import musicbox.MusicBox.model.entity.UserEntity;
import org.springframework.context.ApplicationEvent;
@Getter
public class OnLoginEvent extends ApplicationEvent {
    private final UserEntity user;
    public OnLoginEvent(Object source, UserEntity user) {
        super(source);
        this.user = user;
    }
}
