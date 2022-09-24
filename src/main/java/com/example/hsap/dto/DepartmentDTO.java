package com.example.hsap.dto;

import com.example.hsap.model.DepartmentEntity;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Data;
import java.time.LocalDateTime;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class DepartmentDTO {
    private String id;
    private String name;
    private int asset;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    public DepartmentDTO(DepartmentEntity entity) {
        this.id = entity.getId();
        this.name = entity.getName();
        this.asset = entity.getAsset();
        this.createdAt = entity.getCreatedAt();
        this.updatedAt = entity.getUpdatedAt();
    }
    public static DepartmentEntity toEntity(DepartmentDTO dto) {
        DepartmentEntity entity = new DepartmentEntity();
        entity.setId(dto.getId());
        entity.setName(dto.getName());
        entity.setAsset(dto.getAsset());
        entity.setCreatedAt(dto.getCreatedAt());
        entity.setUpdatedAt(dto.getUpdatedAt());

        return entity;
    }
}
