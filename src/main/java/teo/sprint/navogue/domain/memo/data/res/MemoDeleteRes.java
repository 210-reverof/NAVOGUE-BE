package teo.sprint.navogue.domain.memo.data.res;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemoDeleteRes {
    private int memoId;

    public MemoDeleteRes() {}

    public MemoDeleteRes(int memoId) {
        this.memoId = memoId;
    }
}
