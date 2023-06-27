package teo.sprint.navogue.domain.tag.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import teo.sprint.navogue.domain.memo.data.entity.Memo;
import teo.sprint.navogue.domain.memo.repository.MemoRepository;
import teo.sprint.navogue.domain.tag.data.entity.Tag;
import teo.sprint.navogue.domain.tag.data.entity.TagRelation;
import teo.sprint.navogue.domain.tag.data.req.TagAddReq;
import teo.sprint.navogue.domain.tag.data.req.TagDeleteReq;
import teo.sprint.navogue.domain.tag.data.res.TagAddRes;
import teo.sprint.navogue.domain.tag.data.res.TagDeleteRes;
import teo.sprint.navogue.domain.tag.repository.TagRelationRepository;
import teo.sprint.navogue.domain.tag.repository.TagRepository;
import teo.sprint.navogue.domain.user.data.entity.User;
import teo.sprint.navogue.domain.user.repository.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TagServiceImpl implements TagService {
    private final MemoRepository memoRepository;
    private final TagRepository tagRepository;
    private final UserRepository userRepository;
    private final TagRelationRepository tagRelationRepository;
    @Override
    public TagAddRes addTag(TagAddReq tagAddReq) throws Exception {
        Memo memo = memoRepository.findById(tagAddReq.getMemoId()).get();

        for (String name : tagAddReq.getTagNames()) {
            if (tagRelationRepository.existsByMemoIdAndTagName(tagAddReq.getMemoId(), name)) continue;
            Tag tag = tagRepository.findByName(name);

            if (tag == null) {
                tag = new Tag(name);
                tag = tagRepository.save(tag);
            }

            tagRelationRepository.save(new TagRelation(memo, tag));
        }

        return new TagAddRes(memo.getId());
    }

    @Override
    public TagDeleteRes deleteTag(TagDeleteReq tagDeleteReq) {
        tagRelationRepository.deleteByMemoIdAndTagNames(tagDeleteReq.getMemoId(), tagDeleteReq.getTagNames());

        // TODO :: 스케줄링 삭제 or 태그 쿼리 삭제

        return new TagDeleteRes(tagDeleteReq.getMemoId());
    }

    @Override
    public List<String> getTags(String email) {
        User user = userRepository.findByEmail(email).get();
        return tagRelationRepository.findAllByUser(user.getId());
    }
}