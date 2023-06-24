package teo.sprint.navogue.domain.memo.data.res;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class MemoAddRes {
    private int id;

    public MemoAddRes(int id) {
        this.id = id;
    }
}
