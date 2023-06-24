package teo.sprint.navogue.domain.memo.data.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Test {
    @Id
    int id;

    @Column
    String name;

    public Test() {}
}
