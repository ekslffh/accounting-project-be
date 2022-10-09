package com.example.hsap.controller;

import com.example.hsap.dto.HistoryDTO;
import com.example.hsap.dto.ResponseDTO;
import com.example.hsap.model.HistoryEntity;
import com.example.hsap.model.MemberEntity;
import com.example.hsap.repository.MemberRepository;
import com.example.hsap.security.MemberDetails;
import com.example.hsap.service.HistoryService;
import com.example.hsap.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/history")
@RequiredArgsConstructor
public class HistoryController {
    private final HistoryService historyService;
    private final MemberService memberService;
    private final MemberRepository memberRepository;

    @PostMapping
    public ResponseEntity<?> create(
            @AuthenticationPrincipal MemberDetails principal,
            @RequestBody HistoryDTO dto) {
        try {
                HistoryEntity entity = HistoryDTO.toEntity(dto);
                entity.setId(null);
                MemberEntity memberEntity = memberService.searchById(principal.getUserId());
                entity.setMember(memberEntity);
                entity.setDepartment(memberEntity.getDepartment());
                List<HistoryEntity> entities = historyService.create(entity);
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
        List<HistoryEntity> expenditureEntities = historyService.retrieveAll();
        List<HistoryDTO> expenditureDTOS = expenditureEntities.stream().map(HistoryDTO::new).toList();
        ResponseDTO response = ResponseDTO.<HistoryDTO>builder().data(expenditureDTOS).build();
        return ResponseEntity.ok().body(response);
    }

    @PutMapping
    public ResponseEntity<?> update(
            @AuthenticationPrincipal MemberDetails principal,
            @RequestBody HistoryDTO historyDTO) {
        try {
            HistoryEntity historyEntity = HistoryDTO.toEntity(historyDTO);
            historyEntity.setMember(memberService.searchById(principal.getUserId()));
            List<HistoryEntity> historyEntities = historyService.update(historyEntity);
            List<HistoryDTO> historyDTOS = historyEntities.stream().map(HistoryDTO::new).toList();
            ResponseDTO response = ResponseDTO.<HistoryDTO>builder()
                    .data(historyDTOS)
                    .build();
            return ResponseEntity.ok().body(response);
        } catch (Exception ex) {
            ResponseDTO response = ResponseDTO.builder().error(ex.getMessage()).build();
            return ResponseEntity.badRequest().body(response);
        }
    }

    @DeleteMapping
    public ResponseEntity<?> delete(
            @AuthenticationPrincipal MemberDetails principal,
            @RequestBody HistoryDTO history) {
        try {
            HistoryEntity historyEntity = HistoryEntity.builder().id(history.getId()).build();
//            HistoryEntity historyEntity = HistoryDTO.toEntity(history);
            historyService.delete(historyEntity);
            MemberEntity member = memberService.searchById(principal.getUserId());
            List<HistoryEntity> historyEntities = member.getHistories();
            List<HistoryDTO> historyDTOS = historyEntities.stream().map(HistoryDTO::new).toList();
            ResponseDTO response = ResponseDTO.<HistoryDTO>builder()
                    .data(historyDTOS)
                    .build();
            return ResponseEntity.ok().body(response);
        } catch (Exception ex) {
            ResponseDTO response = ResponseDTO.builder().error(ex.getMessage()).build();
            return ResponseEntity.badRequest().body(response);
        }
    }
}
