package com.example.hsap.dto;

import com.example.hsap.model.ExpenditureEntity;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ExpenditureDTO {

    private String id;
    // 사용일
    private LocalDateTime useDate;

    // 적요
    private String category;

    // "수입" 또는 "지출"
    private String inOrOut;

    // 금액
    private int amountOfMoney;

    // 비고
    private String memo;

    // 유저정보
    private String userId;

    // 생성일
    private LocalDateTime createdAt;

    // 수정일
    private LocalDateTime updatedAt;

    public ExpenditureDTO(ExpenditureEntity expenditureEntity) {
        this.id = expenditureEntity.getId();
        this.useDate = expenditureEntity.getUseDate();
        this.category = expenditureEntity.getCategory();
        this.inOrOut = expenditureEntity.getInOrOut();
        this.amountOfMoney = expenditureEntity.getAmountOfMoney();
        this.memo = expenditureEntity.getMemo();
        this.userId = expenditureEntity.getUserId();
        this.createdAt = expenditureEntity.getCreatedAt();
        this.updatedAt = expenditureEntity.getUpdatedAt();
    }

    public static ExpenditureEntity toEntity(ExpenditureDTO expenditureDTO) {
        return ExpenditureEntity.builder()
                .id(expenditureDTO.getId())
                .useDate(expenditureDTO.getUseDate())
                .category(expenditureDTO.getCategory())
                .inOrOut(expenditureDTO.getInOrOut())
                .amountOfMoney(expenditureDTO.amountOfMoney)
                .memo(expenditureDTO.getMemo())
                .userId(expenditureDTO.getUserId())
                .createdAt(expenditureDTO.getCreatedAt())
                .updatedAt(expenditureDTO.getUpdatedAt())
                .build();
    }

}
