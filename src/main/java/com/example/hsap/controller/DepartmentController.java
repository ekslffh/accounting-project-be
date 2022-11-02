package com.example.hsap.controller;

import com.example.hsap.dto.*;
import com.example.hsap.model.CategoryEntity;
import com.example.hsap.model.DepartmentEntity;
import com.example.hsap.model.HistoryEntity;
import com.example.hsap.model.MemberEntity;
import com.example.hsap.security.MemberDetails;
import com.example.hsap.service.DepartmentService;
import com.example.hsap.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/department")
@RequiredArgsConstructor
public class DepartmentController {
    private final DepartmentService departmentService;
    private final MemberService memberService;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN')")
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

    // 이름으로 해당 부서 조회
    @GetMapping
    public ResponseEntity<?> retrieve(@RequestParam String name) {
        try {
            DepartmentEntity departmentEntity = departmentService.retrieve(name);
            DepartmentDTO departmentDTO = new DepartmentDTO(departmentEntity);
            return ResponseEntity.ok().body(departmentDTO);
        } catch (Exception exception) {
            ResponseDTO response = ResponseDTO.builder().error(exception.getMessage()).build();
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PutMapping
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<?> update(@RequestBody DepartmentDTO dto) {
        try {
            DepartmentEntity entity = DepartmentEntity.builder().name(dto.getName()).id(dto.getId()).build();
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
    @PreAuthorize("hasAnyRole('ADMIN')")
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
    @PreAuthorize("hasAnyRole('LEADER')")
    public ResponseEntity<?> getMembers(@RequestParam String name) {
        try {
            List<MemberEntity> entities = departmentService.getMembers(name);
            List<MemberDTO> dtos = entities.stream().map(MemberDTO::new).toList();
            ResponseDTO response = ResponseDTO.<MemberDTO>builder().data(dtos).build();
            return ResponseEntity.ok().body(response);
        } catch (Exception e) {
            ResponseDTO response = ResponseDTO.builder().error(e.getMessage()).build();
            return ResponseEntity.badRequest().body(response);
        }
    }

    // 부서별 카테고리들 조회 (name 필요)
    @GetMapping("/categories")
    public ResponseEntity<?> getCategories(@RequestParam String name) {
        try {
            List<CategoryEntity> entities = departmentService.getCategories(name);
            List<CategoryDTO> dtos = entities.stream().map(CategoryDTO::new).toList();
            ResponseDTO response = ResponseDTO.<CategoryDTO>builder().data(dtos).build();
            return ResponseEntity.ok().body(response);
        } catch (Exception e) {
            ResponseDTO response = ResponseDTO.builder().error(e.getMessage()).build();
            return ResponseEntity.badRequest().body(response);
        }
    }

    // 부서별 사용내역 조회 (name 필요)
    @GetMapping("/histories")
    @PreAuthorize("hasAnyRole('LEADER')")
    public ResponseEntity<?> getHistories(@RequestParam String name, @AuthenticationPrincipal MemberDetails principal) {
        // 관리자 권한이 아닌 리더의 권한으로 다른 부서의 내역을 확인할 수 없다.
        if (principal.getAuthorities().stream().findFirst().get().toString().equals("ROLE_LEADER") && !memberService.searchById(principal.getUserId()).getDepartment().getName().equals(name))
            throw new AccessDeniedException("본인이 속한 부서 이외에 다른 부서 열람의 권한이 없습니다.");
        try {
            List<HistoryEntity> entities = departmentService.getHistories(name);
            List<HistoryDTO> dtos = entities.stream().map(HistoryDTO::new).toList();
            ResponseDTO response = ResponseDTO.<HistoryDTO>builder().data(dtos).build();
            return ResponseEntity.ok().body(response);
        } catch (Exception e) {
            ResponseDTO response = ResponseDTO.builder().error(e.getMessage()).build();
            return ResponseEntity.badRequest().body(response);
        }
    }

}
