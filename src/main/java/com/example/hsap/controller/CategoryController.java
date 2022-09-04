package com.example.hsap.controller;

import com.example.hsap.dto.CategoryDTO;
import com.example.hsap.dto.DepartmentDTO;
import com.example.hsap.dto.HistoryDTO;
import com.example.hsap.dto.ResponseDTO;
import com.example.hsap.model.CategoryEntity;
import com.example.hsap.model.HistoryEntity;
import com.example.hsap.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/category")
public class CategoryController {
    @Autowired
    CategoryService categoryService;

    @PostMapping
    public ResponseEntity<?> create(@RequestBody CategoryDTO categoryDTO) {
        try {
            CategoryEntity categoryEntity = CategoryDTO.toEntity(categoryDTO);
            List<CategoryEntity> entities = categoryService.create(categoryEntity);
            List<CategoryDTO> dtos = entities.stream().map(CategoryDTO::new).toList();
            ResponseDTO response = ResponseDTO.<CategoryDTO>builder().data(dtos).build();
            return ResponseEntity.ok().body(response);
        } catch (Exception ex) {
            ResponseDTO response = ResponseDTO.builder().error(ex.getMessage()).build();
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping
    public ResponseEntity<?> retrieveByDepartment(@RequestBody DepartmentDTO dto) {
        try {
            List<CategoryEntity> entities = categoryService.retrieveByDepartment(dto.getName());
            List<CategoryDTO> dtos = entities.stream().map(CategoryDTO::new).toList();
            ResponseDTO response = ResponseDTO.<CategoryDTO>builder().data(dtos).build();
            return ResponseEntity.ok().body(response);
        } catch (Exception ex) {
            ResponseDTO response = ResponseDTO.builder().error(ex.getMessage()).build();
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PutMapping
    public ResponseEntity<?> update(@RequestBody CategoryDTO categoryDTO) {
        try {
            CategoryEntity categoryEntity = CategoryDTO.toEntity(categoryDTO);
            List<CategoryEntity> entities = categoryService.update(categoryEntity);
            List<CategoryDTO> dtos = entities.stream().map(CategoryDTO::new).toList();
            ResponseDTO response = ResponseDTO.<CategoryDTO>builder()
                    .data(dtos)
                    .build();
            return ResponseEntity.ok().body(response);
        } catch (Exception ex) {
            ResponseDTO response = ResponseDTO.builder().error(ex.getMessage()).build();
            return ResponseEntity.badRequest().body(response);
        }
    }

    @DeleteMapping
    public ResponseEntity<?> delete(@RequestBody CategoryDTO categoryDTO) {
        try {
            CategoryEntity categoryEntity = CategoryDTO.toEntity(categoryDTO);
            List<CategoryEntity> entities = categoryService.delete(categoryEntity);
            List<CategoryDTO> dtos = entities.stream().map(CategoryDTO::new).toList();
            ResponseDTO response = ResponseDTO.<CategoryDTO>builder()
                    .data(dtos)
                    .build();
            return ResponseEntity.ok().body(response);
        } catch (Exception ex) {
            ResponseDTO response = ResponseDTO.builder().error(ex.getMessage()).build();
            return ResponseEntity.badRequest().body(response);
        }
    }

    // id 필수!
    @GetMapping("/histories")
    public ResponseEntity<?> getHistories(@RequestBody CategoryDTO categoryDTO) {
            try {
                CategoryEntity categoryEntity = CategoryDTO.toEntity(categoryDTO);
                List<HistoryEntity> expenditureEntities = categoryService.getHistories(categoryEntity);
                List<HistoryDTO> expenditureDTOS = expenditureEntities.stream().map(HistoryDTO::new).toList();
                ResponseDTO response = ResponseDTO.<HistoryDTO>builder()
                        .data(expenditureDTOS)
                        .build();
                return ResponseEntity.ok().body(response);
            } catch (Exception ex) {
                ResponseDTO response = ResponseDTO.builder().error(ex.getMessage()).build();
                return ResponseEntity.badRequest().body(response);
            }
    }

}
