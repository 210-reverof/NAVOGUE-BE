package teo.sprint.navogue.domain.memo.data.entity;

import jakarta.persistence.*;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

@Entity
public class Memo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    // TODO :: 연관시키기
    // @ManyToOne
    private int userId;

    @Column
    private String content;

    @Column
    @Enumerated(EnumType.STRING)
    private ContentType contentType;

    @Column
    private boolean isPinned;

    @OneToMany(mappedBy = "memo")
    private List<TagRelation> tagRelations;

    private LocalDateTime createdAt;

    @Builder
    public Memo(int id, int userId, String content, boolean isPinned, LocalDateTime createdAt) {
        this.id = id;
        this.userId = userId;
        this.content = content;
        this.isPinned = isPinned;
        this.createdAt = createdAt;
    }
}
