package teo.sprint.navogue.domain.memo.service;

import org.springframework.data.domain.Slice;
import teo.sprint.navogue.domain.memo.data.req.MemoAddReq;
import teo.sprint.navogue.domain.memo.data.res.MemoAddRes;
import teo.sprint.navogue.domain.memo.data.res.MemoListRes;

public interface MemoService {
    MemoAddRes addMemo(MemoAddReq memoAddReq, String email) throws Exception;

    Slice<MemoListRes> getList(int page, String type, String tag, String keyword, String email);

    int pin(int memoId);
}
