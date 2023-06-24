package teo.sprint.navogue.domain.tag.data.res;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TagAddRes {
    private int memoId;

    public TagAddRes() {}

    public TagAddRes(int memoId) {
        this.memoId = memoId;
    }
}
