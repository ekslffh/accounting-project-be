package com.example.hsap.model;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Where;
import javax.persistence.*;
import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "category")
@Where(clause = "deleted = false")
public class CategoryEntity extends BaseEntity{
    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    private String id;

    // 중복 가능 : 부서별로 같은 이름의 카테고리를 가질 수 있다. ex) a부서와 b부서는 둘다 '전체식사'라는 카테고리를 가질 수 있다.
    @Column(nullable = false)
    private String title;

    private String description;

    private int amount; // 금액

    private boolean deleted = Boolean.FALSE;

    @ManyToOne
    private DepartmentEntity department;

    @OneToMany(mappedBy = "category")
    @ToString.Exclude
    private List<HistoryEntity> histories;
}
