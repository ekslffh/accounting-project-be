package com.example.hsap.dto;

import com.example.hsap.model.CategoryEntity;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Data;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class CategoryDTO {
    private String id;
    private String title;
    private String description;
    private DepartmentDTO department;

    public CategoryDTO(CategoryEntity entity) {
        this.id = entity.getId();
        this.title = entity.getTitle();
        this.description = entity.getDescription();
        this.department = new DepartmentDTO(entity.getDepartment());
    }
    public static CategoryEntity toEntity(CategoryDTO dto) {
        return CategoryEntity.builder()
                .id(dto.getId())
                .title(dto.getTitle())
                .description(dto.getDescription())
                .department(DepartmentDTO.toEntity(dto.getDepartment()))
                .build();
    }
}
