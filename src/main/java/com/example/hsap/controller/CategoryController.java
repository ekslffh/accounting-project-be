package com.example.hsap.controller;

import com.example.hsap.dto.CategoryDTO;
import com.example.hsap.dto.HistoryDTO;
import com.example.hsap.dto.ResponseDTO;
import com.example.hsap.model.CategoryEntity;
import com.example.hsap.model.DepartmentEntity;
import com.example.hsap.model.HistoryEntity;
import com.example.hsap.service.CategoryService;
import com.example.hsap.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/category")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;

    private final MemberService memberService;

    @PostMapping
    @PreAuthorize("hasAnyRole('LEADER')")
    public ResponseEntity<?> create(
            @RequestBody CategoryDTO categoryDTO) {
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
    public ResponseEntity<?> retrieveByDepartment(@RequestParam String name) {
        try {
            List<CategoryEntity> entities = categoryService.retrieveByDepartment(name);
            List<CategoryDTO> dtos = entities.stream().map(CategoryDTO::new).toList();
            ResponseDTO response = ResponseDTO.<CategoryDTO>builder().data(dtos).build();
            return ResponseEntity.ok().body(response);
        } catch (Exception ex) {
            ResponseDTO response = ResponseDTO.builder().error(ex.getMessage()).build();
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PutMapping
    @PreAuthorize("hasAnyRole('LEADER')")
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
    @PreAuthorize("hasAnyRole('LEADER')")
    public ResponseEntity<?> delete(
            @RequestBody CategoryDTO categoryDTO) {
        try {
            DepartmentEntity department = DepartmentEntity.builder().name(categoryDTO.getDepartment().getName()).build();
            CategoryEntity categoryEntity = CategoryEntity.builder()
                    .id(categoryDTO.getId())
                    .department(department)
                    .build();
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
    @PreAuthorize("hasAnyRole('LEADER')")
    public ResponseEntity<?> getHistories(@RequestParam String id) {
            try {
                List<HistoryEntity> expenditureEntities = categoryService.getHistories(id);
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
