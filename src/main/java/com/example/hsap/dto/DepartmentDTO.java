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
    private String notice;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public DepartmentDTO(DepartmentEntity entity) {
        this.id = entity.getId();
        this.name = entity.getName();
        this.asset = entity.getAsset();
        this.notice = entity.getNotice();
        this.createdAt = entity.getCreatedAt();
        this.updatedAt = entity.getUpdatedAt();
    }
    public static DepartmentEntity toEntity(DepartmentDTO dto) {
        DepartmentEntity entity = new DepartmentEntity();
        entity.setId(dto.getId());
        entity.setName(dto.getName());
        entity.setAsset(dto.getAsset());
        entity.setNotice(dto.getNotice());
        entity.setCreatedAt(dto.getCreatedAt());
        entity.setUpdatedAt(dto.getUpdatedAt());

        return entity;
    }
}
