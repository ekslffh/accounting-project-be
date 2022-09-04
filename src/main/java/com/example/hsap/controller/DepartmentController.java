package com.example.hsap.controller;

import com.example.hsap.dto.*;
import com.example.hsap.model.DepartmentEntity;
import com.example.hsap.service.DepartmentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/department")
public class DepartmentController {
    @Autowired
    private DepartmentService departmentService;

    @PostMapping
    public ResponseEntity<?> create(@RequestBody DepartmentDTO dto) {
       try {
           DepartmentEntity entity = DepartmentDTO.toEntity(dto);
           // 생성 시에는 id : null
           entity.setId(null);
           List<DepartmentEntity> entities = departmentService.create(entity);
           List<DepartmentDTO> dtos = entities.stream().map(DepartmentDTO::new).toList();
           ResponseDTO response = ResponseDTO.<DepartmentDTO>builder()
                   .data(dtos)
                   .build();
           return ResponseEntity.ok().body(response);
       } catch (Exception ex) {
           ResponseDTO response = ResponseDTO.builder()
                   .error(ex.getMessage())
                   .build();
           return ResponseEntity.badRequest().body(response);
       }
    }

    // 전체 부서 조회
    @GetMapping("/all")
    public ResponseEntity<?> retrieveAll() {
        List<DepartmentEntity> entities = departmentService.retrieveAll();
        List<DepartmentDTO> dtos = entities.stream().map(DepartmentDTO::new).toList();
        ResponseDTO response = ResponseDTO.<DepartmentDTO>builder()
                .data(dtos)
                .build();
        return ResponseEntity.ok().body(response);
    }

    // 해당 부서 조회
    @GetMapping
    public ResponseEntity<?> retrieve(@RequestParam String name) {
        DepartmentEntity entity = departmentService.retrieve(name);
        if (entity != null) {
            DepartmentDTO dto = new DepartmentDTO(entity);
            return ResponseEntity.ok().body(dto);
        }
        else {
            ResponseDTO response = ResponseDTO.builder().error("This Department is not exists").build();
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PutMapping
    public ResponseEntity<?> update(@RequestBody DepartmentDTO dto) {
        try {
            DepartmentEntity entity = DepartmentDTO.toEntity(dto);
            List<DepartmentEntity> entities = departmentService.update(entity);
            List<DepartmentDTO> dtos = entities.stream().map(DepartmentDTO::new).toList();
            ResponseDTO response = ResponseDTO.<DepartmentDTO>builder()
                    .data(dtos)
                    .build();
            return ResponseEntity.ok().body(response);
        } catch (Exception ex) {
            ResponseDTO response = ResponseDTO.builder()
                    .error(ex.getMessage())
                    .build();
            return ResponseEntity.badRequest().body(response);
        }
    }

    @DeleteMapping
    public ResponseEntity<?> delete(@RequestBody DepartmentDTO dto) {
        try {
            DepartmentEntity entity = DepartmentDTO.toEntity(dto);
            List<DepartmentEntity> entities = departmentService.delete(entity);
            List<DepartmentDTO> dtos = entities.stream().map(DepartmentDTO::new).toList();
            ResponseDTO response = ResponseDTO.<DepartmentDTO>builder()
                    .data(dtos)
                    .build();
            return ResponseEntity.ok().body(response);
        } catch (Exception ex) {
            ResponseDTO response = ResponseDTO.builder().error(ex.getMessage()).build();
            return ResponseEntity.badRequest().body(response);
        }
    }

    // 부서별 멤버들 조회 (name 필요)
    @GetMapping("/members")
    public List<MemberDTO> getMembers(@RequestBody DepartmentEntity entity) {
        return departmentService.getMembers(entity).stream().map(MemberDTO::new).toList();
    }

    // 부서별 카테고리들 조회 (name 필요)
    @GetMapping("/categories")
    public List<CategoryDTO> getCategories(@RequestBody DepartmentEntity entity) {
        return departmentService.getCategories(entity).stream().map(CategoryDTO::new).toList();
    }

    // 부서별 사용내역 조회 (name 필요)
    @GetMapping("/histories")
    public List<HistoryDTO> getHistories(@RequestBody DepartmentEntity entity) {
        return departmentService.getHistories(entity).stream().map(HistoryDTO::new).toList();
    }
}