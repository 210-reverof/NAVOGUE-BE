package teo.sprint.navogue.domain.memo.data.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@ToString
public class OpenGraph {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @OneToOne
    private Memo memo;

    @Column(columnDefinition = "text")
    private String ogTitle;

    @Column(columnDefinition = "text")
    private String ogDesc;

    @Column(columnDefinition = "text")
    private String ogImageUrl;

    public OpenGraph() {}

    public OpenGraph(int id, Memo memo, String ogTitle, String ogDesc, String ogImageUrl) {
        this.id = id;
        this.memo = memo;
        this.ogTitle = ogTitle;
        this.ogDesc = ogDesc;
        this.ogImageUrl = ogImageUrl;
    }

}
