package com.example.hsap.controller;

import com.example.hsap.dto.HistoryDTO;
import com.example.hsap.dto.ResponseDTO;
import com.example.hsap.model.HistoryEntity;
import com.example.hsap.model.MemberEntity;
import com.example.hsap.repository.MemberRepository;
import com.example.hsap.service.HistoryService;
import com.example.hsap.service.MemberService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/history")
public class HistoryController {
    @Autowired
    private HistoryService expenditureService;

    @Autowired
    private MemberService memberService;

    @Autowired
    private MemberRepository memberRepository;

    @PostMapping
    public ResponseEntity<?> create(
            @AuthenticationPrincipal String memberId,
            @RequestBody HistoryDTO dto) {
        try {
                HistoryEntity entity = HistoryDTO.toEntity(dto);
                entity.setId(null);
                MemberEntity memberEntity = memberService.searchById(memberId);
                entity.setMember(memberEntity);
                entity.setDepartment(memberEntity.getDepartment());
                List<HistoryEntity> entities = expenditureService.create(entity);
                List<HistoryDTO> dtos = entities.stream().map(HistoryDTO::new).toList();
                ResponseDTO<HistoryDTO> response = ResponseDTO.<HistoryDTO>builder()
                        .data(dtos)
                        .build();
                return ResponseEntity.ok().body(response);
        } catch (Exception ex) {
                ResponseDTO response = ResponseDTO.builder().error(ex.getMessage()).build();
                return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping
    public ResponseEntity<?> retrieveAll() {
        List<HistoryEntity> expenditureEntities = expenditureService.retrieveAll();
        List<HistoryDTO> expenditureDTOS = expenditureEntities.stream().map(HistoryDTO::new).toList();
        ResponseDTO response = ResponseDTO.<HistoryDTO>builder().data(expenditureDTOS).build();
        return ResponseEntity.ok().body(response);
    }

    @PutMapping
    public ResponseEntity<?> update(
            @RequestBody HistoryDTO expenditureDTO) {
        try {
            HistoryEntity expenditureEntity = HistoryDTO.toEntity(expenditureDTO);
            List<HistoryEntity> expenditureEntities = expenditureService.update(expenditureEntity);
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

    @DeleteMapping
    public ResponseEntity<?> delete(@RequestBody HistoryDTO expenditureDTO) {
        try {
            HistoryEntity expenditureEntity = HistoryDTO.toEntity(expenditureDTO);
            List<HistoryEntity> expenditureEntities = expenditureService.delete(expenditureEntity);
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
