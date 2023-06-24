package teo.sprint.navogue.domain.memo.data.req;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemoAddReq {
    private String content;
    private String contentType;

    public MemoAddReq() {
    }

    public MemoAddReq(String content, String contentType) {
        this.content = content;
        this.contentType = contentType;
    }
}
