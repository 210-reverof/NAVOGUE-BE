package teo.sprint.navogue.domain.memo.data.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import teo.sprint.navogue.domain.memo.data.req.MemoAddReq;
import teo.sprint.navogue.domain.tag.data.entity.TagRelation;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
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

    @Column(updatable = true)
    @CreationTimestamp
    private LocalDateTime createdAt;

    public Memo() {}

    @Builder
    public Memo(int id, int userId, String content, boolean isPinned, LocalDateTime createdAt) {
        this.id = id;
        this.userId = userId;
        this.content = content;
        this.isPinned = isPinned;
        this.createdAt = createdAt;
    }

    public Memo(MemoAddReq memoAddReq) {
        this.content = memoAddReq.getContent();
        this.contentType = ContentType.valueOf(memoAddReq.getContentType());
    }
}
