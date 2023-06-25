package teo.sprint.navogue.domain.memo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import teo.sprint.navogue.common.security.jwt.JwtTokenProvider;
import teo.sprint.navogue.domain.memo.data.req.MemoAddReq;
import teo.sprint.navogue.domain.memo.data.res.MemoAddRes;
import teo.sprint.navogue.domain.memo.data.res.MemoListRes;
import teo.sprint.navogue.domain.memo.service.MemoService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/memo")
public class MemoController {
    @Autowired
    private MemoService memoService;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @PostMapping
    public ResponseEntity<MemoAddRes> addMemo(@RequestBody MemoAddReq memoAddReq,
                                              @RequestHeader("Access-Token") String accessToken) throws Exception {
        String email = jwtTokenProvider.getEmail(accessToken);
        return ResponseEntity.ok(memoService.addMemo(memoAddReq, email));
    }

    @GetMapping("")
    public ResponseEntity<List<MemoListRes>> getMemoList(@RequestParam("type") Optional<String> typeParam,
                                                                   @RequestParam("tag") Optional<String> tagParam,
                                                                   @RequestParam("keyword") Optional<String> keywordParam,
                                                         @RequestHeader("Access-Token") String accessToken) throws Exception {
        String type = typeParam.orElse("");
        String tag = tagParam.orElse("");
        String keyword = keywordParam.orElse("");
        String email = jwtTokenProvider.getEmail(accessToken);

        return ResponseEntity.ok(memoService.getList(type, tag, keyword, email));
    }
}