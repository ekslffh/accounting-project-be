package com.example.hsap.model;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "department", uniqueConstraints = {@UniqueConstraint(columnNames = "name")})
public class DepartmentEntity extends BaseEntity {

    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    private String id;

    @Column(nullable = false)
    private String name;

    private int asset;

    @Column(length = 100000)
    private String notice = "";

    @OneToMany(mappedBy = "department")
    @ToString.Exclude
    List<MemberEntity> members = new ArrayList<>();

    @OneToMany(mappedBy = "department")
    @ToString.Exclude
    private List<HistoryEntity> histories = new ArrayList<>();

    @OneToMany(mappedBy = "department")
    @ToString.Exclude
    private List<CategoryEntity> categories = new ArrayList<>();

    @OneToMany(mappedBy = "department")
    @ToString.Exclude
    private List<PeopleEntity> peoples = new ArrayList<>();

}
