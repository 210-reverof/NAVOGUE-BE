package teo.sprint.navogue.domain.tag.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import teo.sprint.navogue.domain.tag.data.entity.Tag;

public interface TagRepository extends JpaRepository<Tag, Integer> {

    boolean existsByName(String name);

    Tag findByName(String name);
}
