package musicbox.MusicBox.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

@Component
@SessionScope
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CurrentUser {
    private String name;
    private boolean loggedIn;

    public void clear(){
        loggedIn = false;
        name = null;
    }
}
