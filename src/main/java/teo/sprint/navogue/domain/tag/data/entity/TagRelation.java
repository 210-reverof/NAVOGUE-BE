package teo.sprint.navogue.domain.tag.data.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import teo.sprint.navogue.domain.memo.data.entity.Memo;

@Entity
@Getter
@Setter
public class TagRelation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    private Tag tag;

    @ManyToOne
    private Memo memo;

    public TagRelation() {}

    public TagRelation(int id, Tag tag, Memo memo) {
        this.id = id;
        this.tag = tag;
        this.memo = memo;
    }
}
