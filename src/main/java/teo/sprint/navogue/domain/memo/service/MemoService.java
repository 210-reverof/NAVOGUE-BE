package teo.sprint.navogue.domain.memo.service;

import teo.sprint.navogue.domain.memo.data.req.MemoAddReq;
import teo.sprint.navogue.domain.memo.data.res.MemoAddRes;

public interface MemoService {
    MemoAddRes addMemo(MemoAddReq memoAddReq) throws Exception;
}
