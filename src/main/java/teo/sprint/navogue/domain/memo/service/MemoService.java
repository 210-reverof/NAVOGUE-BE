package teo.sprint.navogue.domain.memo.service;

import teo.sprint.navogue.domain.memo.data.req.MemoAddReq;
import teo.sprint.navogue.domain.memo.data.res.MemoAddRes;
import teo.sprint.navogue.domain.memo.data.res.MemoListRes;

import java.util.List;

public interface MemoService {
    MemoAddRes addMemo(MemoAddReq memoAddReq) throws Exception;

    List<MemoListRes> getList(String type, String tag, String keyword);
}
