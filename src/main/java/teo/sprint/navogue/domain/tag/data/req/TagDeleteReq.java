package teo.sprint.navogue.domain.tag.data.req;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class TagDeleteReq {
    private int memoId;
    private List<String> tagNames;

    public TagDeleteReq() {}

    public TagDeleteReq(int memoId, List<String> tagNames) {
        this.memoId = memoId;
        this.tagNames = tagNames;
    }
}
