package teo.sprint.navogue.domain.tag.service;

import teo.sprint.navogue.domain.tag.data.req.TagAddReq;
import teo.sprint.navogue.domain.tag.data.req.TagDeleteReq;
import teo.sprint.navogue.domain.tag.data.res.TagAddRes;
import teo.sprint.navogue.domain.tag.data.res.TagDeleteRes;

public interface TagService {

    TagAddRes addTag(TagAddReq tagAddReq) throws Exception;

    TagDeleteRes deleteTag(TagDeleteReq tagDeleteReq);
}
