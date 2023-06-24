package teo.sprint.navogue.domain.memo.data.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@ToString
public class OpenGraph {
    @Id
    @OneToOne
    private Memo memo;

    @Column(columnDefinition = "text")
    private String ogTitle;

    @Column(columnDefinition = "text")
    private String ogDesc;

    @Column(columnDefinition = "text")
    private String ogImageUrl;

    public OpenGraph() {}

    public OpenGraph(Memo memo, String ogTitle, String ogDesc, String ogImageUrl) {
        this.memo = memo;
        this.ogTitle = ogTitle;
        this.ogDesc = ogDesc;
        this.ogImageUrl = ogImageUrl;
    }
}
