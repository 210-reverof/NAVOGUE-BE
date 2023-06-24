package teo.sprint.navogue.domain.memo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import teo.sprint.navogue.domain.memo.data.entity.Memo;

public interface MemoRepository extends JpaRepository<Memo, Integer> {
}
