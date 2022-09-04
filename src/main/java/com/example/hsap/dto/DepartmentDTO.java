package com.example.hsap.dto;

import com.example.hsap.model.DepartmentEntity;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Data;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class DepartmentDTO {
    private String id;
    private String name;
    private int asset;
    public DepartmentDTO(DepartmentEntity entity) {
        this.id = entity.getId();
        this.name = entity.getName();
        this.asset = entity.getAsset();
    }
    public static DepartmentEntity toEntity(DepartmentDTO dto) {
        return DepartmentEntity.builder()
                .id(dto.getId())
                .name(dto.getName())
                .asset(dto.getAsset())
                .build();
    }
}
