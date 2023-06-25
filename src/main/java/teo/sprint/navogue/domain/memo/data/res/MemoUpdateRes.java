package teo.sprint.navogue.domain.memo.data.res;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemoUpdateRes {
    private int memoId;

    public MemoUpdateRes() {}

    public MemoUpdateRes(int memoId) {
        this.memoId = memoId;
    }
}
