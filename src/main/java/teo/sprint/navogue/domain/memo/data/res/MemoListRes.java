package teo.sprint.navogue.domain.memo.data.res;

import lombok.Getter;
import lombok.Setter;
import teo.sprint.navogue.domain.memo.data.entity.ContentType;
import teo.sprint.navogue.domain.memo.data.entity.OpenGraph;

import java.time.LocalDateTime;

@Getter
@Setter
public class MemoListRes {
    private int id;
    private String content;
    private ContentType contentType;
    private boolean isPinned;
    private LocalDateTime createdAt;
    private OpenGraph openGraph;


}
