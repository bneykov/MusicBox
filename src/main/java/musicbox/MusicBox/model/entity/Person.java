package musicbox.MusicBox.model.entity;

import jakarta.annotation.Nonnull;
import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@MappedSuperclass
public abstract class Person extends BaseEntity {

    @Nonnull
    @Column(name = "first_name")
    private String firstName;

    @Nonnull
    @Column(name = "last_name")
    private String lastName;

    @Column
    private String image;
}
