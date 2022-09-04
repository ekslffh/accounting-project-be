package com.example.hsap.model;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "member", uniqueConstraints = {@UniqueConstraint(columnNames = "email")})
public class MemberEntity {
    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    private String id;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String name;

    private int asset;

    private String birth;

    private String gender;

    private LocalDateTime joinedAt;

    @OneToMany(mappedBy = "member")
    @ToString.Exclude
    private List<HistoryEntity> expenditures = new ArrayList<HistoryEntity>();

    @ManyToOne
    @JoinColumn(name = "department_id")
    private DepartmentEntity department;
}
