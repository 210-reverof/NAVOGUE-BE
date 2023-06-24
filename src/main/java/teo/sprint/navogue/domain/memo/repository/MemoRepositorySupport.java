package teo.sprint.navogue.domain.memo.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;
import teo.sprint.navogue.domain.memo.data.entity.ContentType;
import teo.sprint.navogue.domain.memo.data.entity.Memo;
import teo.sprint.navogue.domain.memo.data.entity.QMemo;
import teo.sprint.navogue.domain.memo.data.res.MemoListRes;
import teo.sprint.navogue.domain.tag.data.entity.QTag;
import teo.sprint.navogue.domain.tag.data.entity.QTagRelation;

import java.util.List;

@Repository
public class MemoRepositorySupport extends QuerydslRepositorySupport {
    @Autowired
    private final JPAQueryFactory jpaQueryFactory;

    public MemoRepositorySupport(JPAQueryFactory jpaQueryFactory) {
        super(Memo.class);
        this.jpaQueryFactory = jpaQueryFactory;
    }
    public List<MemoListRes> getList(String type, String tag, String keyword) {
        QMemo m = QMemo.memo;
        QTagRelation tr = QTagRelation.tagRelation;
        QTag t = QTag.tag;

        BooleanBuilder builder = new BooleanBuilder();

        if (!type.equals("")) builder.and(m.contentType.eq(ContentType.valueOf(type)));
        if (!keyword.equals("")) builder.and(m.content.contains(keyword));
        if (!tag.equals("")) {
            builder.and(m.id.in(
                    JPAExpressions.select(tr.memo.id)
                            .from(tr)
                            .join(tr.tag, t)
                            .where(t.name.eq(tag))
            ));
        }

        List<MemoListRes> memoListResList = jpaQueryFactory
                .select(Projections.constructor(MemoListRes.class, m))
                .from(m)
                .where(builder)
                .fetch();

        memoListResList.forEach(memoListRes -> {
            List<String> tagList = jpaQueryFactory
                    .select(t.name)
                    .from(tr)
                    .join(tr.tag, t)
                    .where(tr.memo.id.eq(memoListRes.getId()))
                    .fetch();
            memoListRes.setTags(tagList);
        });

        return memoListResList;
    }
}