package teo.sprint.navogue.domain.tag.data.req;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class TagAddReq {
    private int memoId;
    private List<String> tagNames;

    public TagAddReq() {}
}
