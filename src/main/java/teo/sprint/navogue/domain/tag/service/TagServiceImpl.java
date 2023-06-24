package teo.sprint.navogue.domain.tag.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import teo.sprint.navogue.domain.memo.data.entity.Memo;
import teo.sprint.navogue.domain.memo.repository.MemoRepository;
import teo.sprint.navogue.domain.tag.data.entity.Tag;
import teo.sprint.navogue.domain.tag.data.entity.TagRelation;
import teo.sprint.navogue.domain.tag.data.req.TagAddReq;
import teo.sprint.navogue.domain.tag.data.res.TagAddRes;
import teo.sprint.navogue.domain.tag.repository.TagRelationRepository;
import teo.sprint.navogue.domain.tag.repository.TagRepository;

@Service
@RequiredArgsConstructor
public class TagServiceImpl implements TagService {
    private final MemoRepository memoRepository;
    private final TagRepository tagRepository;
    private final TagRelationRepository tagRelationRepository;
    @Override
    public TagAddRes addTag(TagAddReq tagAddReq) throws Exception {
        Memo memo = memoRepository.findById(tagAddReq.getMemoId()).get();

        for (String name : tagAddReq.getTagNames()) {
            Tag tag = tagRepository.findByName(name);

            if (tag == null) {
                tag = new Tag(name);
                tag = tagRepository.save(tag);
            }

            tagRelationRepository.save(new TagRelation(memo, tag));
        }

        return new TagAddRes(memo.getId());
    }
}
