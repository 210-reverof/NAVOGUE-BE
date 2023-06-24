package teo.sprint.navogue.domain.tag.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import teo.sprint.navogue.domain.tag.data.entity.TagRelation;

public interface TagRelationRepository extends JpaRepository<TagRelation, Integer> {
}
