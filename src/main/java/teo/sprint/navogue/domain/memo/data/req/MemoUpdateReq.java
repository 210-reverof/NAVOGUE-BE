package teo.sprint.navogue.domain.memo.data.req;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemoUpdateReq {
    private int id;
    private String content;

    public MemoUpdateReq() {}

    public MemoUpdateReq(int id, String content) {
        this.id = id;
        this.content = content;
    }
}
