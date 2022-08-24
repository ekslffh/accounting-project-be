package com.example.hsap.controller;

import com.example.hsap.dto.ExpenditureDTO;
import com.example.hsap.dto.ResponseDTO;
import com.example.hsap.model.ExpenditureEntity;
import com.example.hsap.service.ExpenditureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/expenditure")
public class ExpenditureController {

    @Autowired
    private ExpenditureService expenditureService;

    @PostMapping
    public ResponseEntity<?> create(@RequestBody ExpenditureDTO dto) {
        try {
            ExpenditureEntity entity = ExpenditureDTO.toEntity(dto);
            entity.setUserId("temporary-user");
            List<ExpenditureEntity> entities = expenditureService.create(entity);
            List<ExpenditureDTO> dtos = entities.stream().map(ExpenditureDTO::new).toList();
            ResponseDTO<ExpenditureDTO> response = ResponseDTO.<ExpenditureDTO>builder()
                    .data(dtos)
                    .build();
            return ResponseEntity.ok().body(response);
        } catch (Exception ex) {
            ResponseDTO response = ResponseDTO.builder().error(ex.getMessage()).build();
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping
    public ResponseEntity<?> retrieve() {
        String user = "temporary-user";
        List<ExpenditureEntity> entities = expenditureService.retrieve(user);
        List<ExpenditureDTO> dtos = entities.stream().map(ExpenditureDTO::new).toList();
        ResponseDTO<ExpenditureDTO> response = ResponseDTO.<ExpenditureDTO>builder()
                .data(dtos)
                .build();
        return ResponseEntity.ok().body(response);
    }

    @PutMapping
    public ResponseEntity<?> update(@RequestBody ExpenditureDTO dto) {
        try {
            ExpenditureEntity entity = ExpenditureDTO.toEntity(dto);
            entity.setUserId("temporary-user");
            List<ExpenditureEntity> entities = expenditureService.update(entity);
            List<ExpenditureDTO> dtos = entities.stream().map(ExpenditureDTO::new).toList();
            ResponseDTO<ExpenditureDTO> response = ResponseDTO.<ExpenditureDTO>builder()
                    .data(dtos)
                    .build();
            return ResponseEntity.ok().body(response);
        } catch (Exception ex) {
            ResponseDTO response = ResponseDTO.builder().error(ex.getMessage()).build();
            return ResponseEntity.badRequest().body(response);
        }
    }

    @DeleteMapping
    public ResponseEntity<?> delete(@RequestBody ExpenditureDTO dto) {
        try {
            ExpenditureEntity entity = ExpenditureDTO.toEntity(dto);
            entity.setUserId("temporary-user");
            List<ExpenditureEntity> entities = expenditureService.delete(entity);
            List<ExpenditureDTO> dtos = entities.stream().map(ExpenditureDTO::new).toList();
            ResponseDTO<ExpenditureDTO> response = ResponseDTO.<ExpenditureDTO>builder()
                    .data(dtos)
                    .build();
            return ResponseEntity.ok().body(response);
        } catch (Exception ex) {
            ResponseDTO response = ResponseDTO.builder().error(ex.getMessage()).build();
            return ResponseEntity.badRequest().body(response);
        }
    }

}
