package musicbox.MusicBox.model.enums;

import lombok.Getter;

@Getter
public enum Genre {
    ROCK("Rock"),
    POP("Pop"),
    JAZZ("Jazz"),
    ELECTRONIC("Electronic"),
    COUNTRY("Country");

    private final String value;

    Genre(String value) {
        this.value = value;
    }

}
