package teo.sprint.navogue.domain.tag.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import teo.sprint.navogue.common.security.jwt.JwtTokenProvider;
import teo.sprint.navogue.domain.tag.data.req.TagAddReq;
import teo.sprint.navogue.domain.tag.data.req.TagDeleteReq;
import teo.sprint.navogue.domain.tag.data.res.TagAddRes;
import teo.sprint.navogue.domain.tag.data.res.TagDeleteRes;
import teo.sprint.navogue.domain.tag.service.TagService;

import java.util.List;

@RestController
@RequestMapping("/tag")
public class TagController {

    @Autowired
    private TagService tagService;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @PostMapping
    public ResponseEntity<TagAddRes> addTag(@RequestBody TagAddReq tagAddReq) throws Exception{
        return ResponseEntity.ok(tagService.addTag(tagAddReq));
    }

    @DeleteMapping
    public ResponseEntity<TagDeleteRes> deleteTag(@RequestParam("tagName") List<String> tagNames, @RequestParam("memoId") int memoId) throws Exception{
        return ResponseEntity.ok(tagService.deleteTag(new TagDeleteReq(memoId, tagNames)));
    }

    @GetMapping
    public ResponseEntity<List<String>> getTags(@RequestHeader("Access-Token") String accessToken) {
        String email = jwtTokenProvider.getEmail(accessToken);
        return ResponseEntity.ok(tagService.getTags(email));
    }
}
