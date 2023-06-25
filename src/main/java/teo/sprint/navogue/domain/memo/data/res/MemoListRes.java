package teo.sprint.navogue.domain.memo.data.res;

import lombok.Getter;
import lombok.Setter;
import teo.sprint.navogue.domain.memo.data.entity.ContentType;
import teo.sprint.navogue.domain.memo.data.entity.Memo;
import teo.sprint.navogue.domain.memo.data.entity.OpenGraph;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class MemoListRes {
    private int id;
    private String content;
    private ContentType contentType;
    private boolean isPinned;
    private LocalDateTime createdAt;
    private List<String> tags = new ArrayList<>();
    private OpenGraph openGraph;

    public MemoListRes(int id) {}

    public MemoListRes(Memo memo) {
        this.id = memo.getId();
        this.content = memo.getContent();
        this.contentType = memo.getContentType();
        this.isPinned = memo.isPinned();
        this.createdAt = memo.getCreatedAt();
    }
}