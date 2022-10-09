package com.example.hsap.dto;

import com.example.hsap.model.CategoryEntity;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Data;
import java.time.LocalDateTime;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class CategoryDTO {
    private String id;
    private String title;
    private String description;
    private DepartmentDTO department;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public CategoryDTO(CategoryEntity entity) {
        this.id = entity.getId();
        this.title = entity.getTitle();
        this.description = entity.getDescription();
        this.department = new DepartmentDTO(entity.getDepartment());
        this.createdAt = entity.getCreatedAt();
        this.updatedAt = entity.getUpdatedAt();
    }
    public static CategoryEntity toEntity(CategoryDTO dto) {
        CategoryEntity entity = new CategoryEntity();
        entity.setId(dto.getId());
        entity.setTitle(dto.getTitle());
        entity.setDescription(dto.getDescription());
        entity.setDepartment(DepartmentDTO.toEntity(dto.getDepartment()));
        entity.setCreatedAt(dto.getCreatedAt());
        entity.setUpdatedAt(dto.getUpdatedAt());

        return entity;
    }
}
