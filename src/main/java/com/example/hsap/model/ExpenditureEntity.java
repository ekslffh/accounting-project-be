package com.example.hsap.model;

import javax.persistence.*;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDateTime;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "Expenditure")
public class ExpenditureEntity {
    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    private String id;

    @Column(nullable = false)
    // 사용일
    private LocalDateTime useDate;

    @Column(nullable = false)
    // 적요
    private String category;

    @Column(nullable = false)
    // "수입" 또는 "지출"
    private String inOrOut;

    @Column(nullable = false)
    // 금액
    private int amountOfMoney;

    // 비고
    private String memo;

    @Column(nullable = false)
    // 유저정보
    private String userId;

    // 생성일
    private LocalDateTime createdAt;

    // 수정일
    private LocalDateTime updatedAt;

}
