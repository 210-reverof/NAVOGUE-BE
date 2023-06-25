package teo.sprint.navogue.domain.memo.data.res;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemoPinRes {
    private int memoId;

    public MemoPinRes() {}

    public MemoPinRes(int memoId) {
        this.memoId = memoId;
    }
}
