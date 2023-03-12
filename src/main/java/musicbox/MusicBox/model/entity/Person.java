package musicbox.MusicBox.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@MappedSuperclass
public abstract class Person extends BaseEntity {


    @Column(name = "name")
    private String name;


    @Column()
    private String imageUrl;
    @PrePersist
    public void setDefaultValue(){
        if (this.imageUrl == null) {
            this.imageUrl = "/images/profile_picture.png";
        }
    }
}
