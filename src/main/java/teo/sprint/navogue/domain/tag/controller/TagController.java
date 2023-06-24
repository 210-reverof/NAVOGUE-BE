package teo.sprint.navogue.domain.tag.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import teo.sprint.navogue.domain.tag.data.req.TagAddReq;
import teo.sprint.navogue.domain.tag.data.res.TagAddRes;
import teo.sprint.navogue.domain.tag.service.TagService;

@RestController
@RequestMapping("/tag")
public class TagController {

    @Autowired
    private TagService tagService;

    @PostMapping
    public ResponseEntity<TagAddRes> addTag(@RequestBody TagAddReq tagAddReq) throws Exception{
        return ResponseEntity.ok(tagService.addTag(tagAddReq));
    }
}
