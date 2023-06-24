package teo.sprint.navogue.domain.memo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import teo.sprint.navogue.domain.memo.data.req.MemoAddReq;
import teo.sprint.navogue.domain.memo.data.res.MemoAddRes;
import teo.sprint.navogue.domain.memo.data.res.MemoListRes;
import teo.sprint.navogue.domain.memo.service.MemoService;

import java.util.Optional;

@RestController
@RequestMapping("/memo")
public class MemoController {
    @Autowired
    private MemoService memoService;

    @PostMapping
    public ResponseEntity<MemoAddRes> addMemo(@RequestBody MemoAddReq memoAddReq) throws Exception {
        return ResponseEntity.ok(memoService.addMemo(memoAddReq));
    }

    @GetMapping("")
    public ResponseEntity<MemoListRes> getDealListWithOption(@RequestParam("content-type") Optional<String> typeParam,
                                                             @RequestParam("tag") Optional<String> tagParam,
                                                             @RequestParam("keyword") Optional<String> keywordParam) throws Exception {
        String type = typeParam.orElse("");
        String tag = tagParam.orElse("");
        String keyword = keywordParam.orElse("");

        return null;
    }
}
