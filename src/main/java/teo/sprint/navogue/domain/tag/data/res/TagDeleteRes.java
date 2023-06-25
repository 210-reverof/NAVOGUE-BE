package teo.sprint.navogue.domain.tag.data.res;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TagDeleteRes {
    private int memoId;

    public TagDeleteRes() {}

    public TagDeleteRes(int memoId) {
        this.memoId = memoId;
    }
}
