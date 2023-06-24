package teo.sprint.navogue.domain.memo.data.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

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
    private boolean isPinned;

    private LocalDateTime createdAt;

    public Memo(int id, int userId, String content, boolean isPinned, LocalDateTime createdAt) {
        this.id = id;
        this.userId = userId;
        this.content = content;
        this.isPinned = isPinned;
        this.createdAt = createdAt;
    }
}
