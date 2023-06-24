package teo.sprint.navogue.domain.memo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import teo.sprint.navogue.domain.memo.data.entity.Memo;
import teo.sprint.navogue.domain.memo.data.entity.OpenGraph;

public interface OpenGraphRepository extends JpaRepository<OpenGraph, Memo> {
}
