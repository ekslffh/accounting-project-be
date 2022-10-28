package com.example.hsap.controller;

import com.example.hsap.dto.HistoryDTO;
import com.example.hsap.dto.ResponseDTO;
import com.example.hsap.model.HistoryEntity;
import com.example.hsap.model.MemberEntity;
import com.example.hsap.security.MemberDetails;
import com.example.hsap.service.HistoryService;
import com.example.hsap.service.MemberService;
import com.example.hsap.service.S3Upload;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/history")
@RequiredArgsConstructor
public class HistoryController {
    private final HistoryService historyService;
    private final MemberService memberService;
    private final S3Upload s3Upload;

    @PostMapping
    public ResponseEntity<?> create(
            @AuthenticationPrincipal MemberDetails principal,
            List<MultipartFile> receipt,
            @RequestPart("history") HistoryDTO dto) {
        try {
            MemberEntity memberEntity = memberService.searchById(principal.getUserId());
            String path = null;
            if (receipt != null) {
                for (MultipartFile multipartFile : receipt) {
                    path = s3Upload.upload(multipartFile, dto.getUseDate());
                }
            }
//            s3Upload.remove("receipts/627610ef-9509-4bf9-92bf-7d87dbd84395-래서펜더.webp");
            HistoryEntity entity = HistoryDTO.toEntity(dto);
            entity.setId(null);
            if (path != null) entity.setImagePath(path);
            entity.setMember(memberEntity);

            entity.setDepartment(memberEntity.getDepartment());
            List<HistoryEntity> entities = historyService.create(entity);
            List<HistoryDTO> dtos = entities.stream().map(HistoryDTO::new).toList();
            ResponseDTO<HistoryDTO> response = ResponseDTO.<HistoryDTO>builder()
                    .data(dtos)
                    .build();
            return ResponseEntity.ok().body(response);

        } catch (IOException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
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
            List<MultipartFile> receipt,
            @RequestPart("history") HistoryDTO historyDTO) {
        try {
            String path = null;
            if (receipt != null) {
                for (MultipartFile multipartFile : receipt) {
                    path = s3Upload.upload(multipartFile, historyDTO.getUseDate());
                }
            }
            HistoryEntity historyEntity = HistoryDTO.toEntity(historyDTO);
            historyEntity.setImagePath(path);
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
