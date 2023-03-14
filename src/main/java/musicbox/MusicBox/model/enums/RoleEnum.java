package musicbox.MusicBox.model.enums;

import lombok.Getter;
import lombok.Setter;

@Getter
public enum
RoleEnum {
    ADMIN("Admin"), USER("User");
    private final String value;

    RoleEnum(String value) {
        this.value = value;
    }
}
