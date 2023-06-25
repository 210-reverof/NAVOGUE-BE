package teo.sprint.navogue.domain.memo.repository;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import teo.sprint.navogue.domain.memo.data.entity.Memo;
import teo.sprint.navogue.domain.memo.data.entity.OpenGraph;

public interface OpenGraphRepository extends JpaRepository<OpenGraph, Integer> {
    @Transactional
    void deleteByMemoId(int memoId);
}
