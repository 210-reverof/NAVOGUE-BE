package teo.sprint.navogue.domain.memo.repository;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import teo.sprint.navogue.domain.memo.data.entity.Memo;

@Repository
public interface MemoRepository extends JpaRepository<Memo, Integer> {
    @Modifying
    @Transactional
    @Query("UPDATE Memo m SET m.isPinned = CASE WHEN m.isPinned = false THEN true ELSE false END WHERE m.id = :memoId")
    int pinMemo(@Param("memoId") int memoId);

    @Modifying
    @Transactional
    @Query("UPDATE Memo m SET m.content = :content WHERE m.id = :id")
    void updateContent(int id, String content);

    @Transactional
    void deleteById(int id);
}
