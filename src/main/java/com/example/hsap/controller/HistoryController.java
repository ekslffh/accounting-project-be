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
import java.util.ArrayList;
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
            List<MultipartFile> receipts,
            @RequestPart("history") HistoryDTO dto,
            @RequestParam(required = false) String year
            )
    {
        try {
            MemberEntity memberEntity = memberService.searchById(principal.getUserId());
            List<String> path = new ArrayList<>();
            if (receipts != null) {
                for (MultipartFile receipt : receipts) {
                    path.add(s3Upload.upload(receipt, dto.getUseDate()));
                }
            }
            HistoryEntity entity = HistoryDTO.toEntity(dto);
            entity.setId(null);
            if (path.size() != 0) entity.setImagePath(path);
            entity.setMember(memberEntity);

            entity.setDepartment(memberEntity.getDepartment());
            List<HistoryEntity> entities = historyService.create(entity, year);
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

    @DeleteMapping("/receipt")
    public ResponseEntity<?> deleteReceipt(@RequestBody HistoryDTO historyDTO, @RequestParam(required = false) String year) {
        try {
            // 해당 히스토리 객체에서 imagePath 지우기
            List<HistoryEntity> historyEntities = historyService.deleteReceipt(historyDTO.getId(), year);

            List<HistoryDTO> historyDTOS = historyEntities.stream().map(HistoryDTO::new).toList();
            ResponseDTO response = ResponseDTO.<HistoryDTO>builder().data(historyDTOS).build();

            // 실제 S3에서 해당 이미지 삭제 진행
            s3Upload.remove(historyDTO.getImagePath());
            return ResponseEntity.ok().body(response);
        } catch (Exception ex) {
            ResponseDTO response = ResponseDTO.builder().error(ex.getMessage()).build();
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PutMapping
    public ResponseEntity<?> update(
            @AuthenticationPrincipal MemberDetails principal,
            List<MultipartFile> receipts,
            @RequestPart("history") HistoryDTO dto,
            @RequestParam(required = false) String year
            )
    {
        try {
            List<String> path = new ArrayList<>();

            if (receipts != null) {
                for (MultipartFile receipt : receipts) {
                    path.add(s3Upload.update(receipt, dto.getUseDate(), dto.getImagePath()));
                }
            }

            if (path.size() != 0) dto.setImagePath(path);

            HistoryEntity historyEntity = HistoryDTO.toEntity(dto);
            historyEntity.setMember(memberService.searchById(principal.getUserId()));
            List<HistoryEntity> historyEntities = historyService.update(historyEntity, year);
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
            @RequestBody HistoryDTO history,
            @RequestParam(required = false) String year
        )
    {
        try {
            // 실제 S3에서 해당 이미지 삭제 진행
            s3Upload.remove(history.getImagePath());

            HistoryEntity historyEntity = HistoryEntity.builder().id(history.getId()).build();

            historyService.delete(historyEntity);
            MemberEntity member = memberService.searchById(principal.getUserId());
            List<HistoryEntity> historyEntities = member.getHistories();
            historyEntities = historyEntities.stream().filter(history1 -> history1.getUseDate().getYear() == Integer.parseInt(year)).toList();

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
