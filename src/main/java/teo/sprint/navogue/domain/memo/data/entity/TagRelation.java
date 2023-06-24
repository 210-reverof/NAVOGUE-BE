package teo.sprint.navogue.domain.memo.data.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import teo.sprint.navogue.domain.tag.data.entity.Tag;

@Entity
@Getter
@Setter
public class TagRelation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "tag_name")
    private Tag tag;

    @ManyToOne
    @JoinColumn(name = "memo_id")
    private Memo memo;

    public TagRelation() {}

    public TagRelation(Long id, Tag tag, Memo memo) {
        this.id = id;
        this.tag = tag;
        this.memo = memo;
    }
}
