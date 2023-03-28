package musicbox.MusicBox.utils.errors;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ObjectNotFoundException extends RuntimeException {
    private final long id;
    private final String type;


    public ObjectNotFoundException(long id, String type) {
        super("Object with ID " + id + " of type " + type + " not found");
        this.id = id;
        this.type = type;
    }
    public ObjectNotFoundException(String type) {
        super("Object of type " + type + " not found");
        this.type = type;
        this.id = 0;
    }
}
