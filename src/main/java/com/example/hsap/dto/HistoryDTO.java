package com.example.hsap.dto;

import com.example.hsap.model.CategoryEntity;
import com.example.hsap.model.HistoryEntity;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Data;
import java.time.LocalDateTime;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class HistoryDTO {
    private String id;
    private LocalDateTime useDate; // 사용일
    private int income; // 수입
    private int expenditure; // 지출
    private String memo; // 비고
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private MemberDTO member; // 유저정보
    private CategoryDTO category;

    public HistoryDTO(HistoryEntity historyEntity) {
        this.id = historyEntity.getId();
        this.useDate = historyEntity.getUseDate();
        this.income = historyEntity.getIncome();
        this.expenditure = historyEntity.getExpenditure();
        this.member = new MemberDTO(historyEntity.getMember());
        this.memo = historyEntity.getMemo();
        this.category = new CategoryDTO(historyEntity.getCategory());
        this.createdAt = historyEntity.getCreatedAt();
        this.updatedAt = historyEntity.getUpdatedAt();
    }

    public static HistoryEntity toEntity(HistoryDTO expenditureDTO) {
        // category에 아이디만 넣어서 주면 된다.
        CategoryEntity categoryEntity = CategoryEntity.builder().id(expenditureDTO.getCategory().getId()).build();
        return HistoryEntity.builder()
                .id(expenditureDTO.getId())
                .useDate(expenditureDTO.getUseDate())
                .income(expenditureDTO.getIncome())
                .expenditure(expenditureDTO.getExpenditure())
                .memo(expenditureDTO.getMemo())
                .createdAt(expenditureDTO.getCreatedAt())
                .updatedAt(expenditureDTO.getUpdatedAt())
                .category(categoryEntity)
                .build();
    }
}