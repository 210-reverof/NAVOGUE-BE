package teo.sprint.navogue.domain.tag.service;

import teo.sprint.navogue.domain.tag.data.req.TagAddReq;
import teo.sprint.navogue.domain.tag.data.res.TagAddRes;

public interface TagService {

    TagAddRes addTag(TagAddReq tagAddReq) throws Exception;
}
