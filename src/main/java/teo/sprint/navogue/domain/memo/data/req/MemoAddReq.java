package teo.sprint.navogue.domain.memo.data.req;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemoAddReq {
    private String content;

    public MemoAddReq() {
    }

    public MemoAddReq(String content) {
        this.content = content;
    }
}
