package teo.sprint.navogue.domain.tag.data.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Tag {

    @Id
    @Column(length = 100)
    private String name;
    public Tag() {
    }

    public Tag(String name) {
        this.name = name;
    }

}
