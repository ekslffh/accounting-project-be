package com.example.hsap.controller;

import com.example.hsap.dto.DepartmentDTO;
import com.example.hsap.dto.HistoryDTO;
import com.example.hsap.dto.ResponseDTO;
import com.example.hsap.model.DepartmentEntity;
import com.example.hsap.model.HistoryEntity;
import com.example.hsap.model.MemberEntity;
import com.example.hsap.repository.MemberRepository;
import com.example.hsap.security.MemberDetails;
import com.example.hsap.service.HistoryService;
import com.example.hsap.service.MemberService;
import com.example.hsap.service.S3Upload;
import com.fasterxml.jackson.core.JsonParser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Random;

@Slf4j
@RestController
@RequestMapping("/history")
@RequiredArgsConstructor
public class HistoryController {
    private final HistoryService historyService;
    private final MemberService memberService;
    private final S3Upload s3Upload;
    private final MemberRepository memberRepository;

    public static String randomMix(int range) {
        StringBuilder sb = new StringBuilder();
        Random rd = new Random();

        for(int i=0;i<range;i++){

            if(rd.nextBoolean()){
                sb.append(rd.nextInt(10));
            }else {
                sb.append((char)(rd.nextInt(26)+65));
            }
        }
        return sb.toString();
    }
//    출처: https://miraclecat.tistory.com/16 [MiracleCat:티스토리]



    @PostMapping
    public ResponseEntity<?> create(
            @AuthenticationPrincipal MemberDetails principal,
            List<MultipartFile> receipt,
            @RequestPart("history") HistoryDTO dto) {
        try {
            MemberEntity memberEntity = memberService.searchById(principal.getUserId());
//            String UPLOAD_PATH = "/Users/nasungmin/Desktop/accounting-project/receipt/" + memberEntity.getEmail();
            String path = null;
//            String randomStr = null;
            for (int i = 0; i < receipt.size(); i++) {
//                String originName = receipt.get(i).getOriginalFilename();
//                String ext = originName.substring(originName.lastIndexOf('.') + 1);
//
//                randomStr = randomMix(10) + "." + ext;
//                assert originName != null;
//                File folder = new File(UPLOAD_PATH);
//
//                if (!folder.exists()) folder.mkdirs();
//
//                File newFile = new File(UPLOAD_PATH, randomStr);
//                receipt.get(i).transferTo(newFile);
                path = s3Upload.upload(receipt.get(i));
            }
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

    @PostMapping("/file")
    public boolean uploadFiles(List<MultipartFile> files, String hello, @RequestPart("department") DepartmentEntity department, HttpServletRequest request) {
        String UPLOAD_PATH = "/Users/nasungmin/Desktop/accounting-project/image/" + new Date().getTime();

        JSONParser jsonParser = new JSONParser();
        JSONObject jsonObject = new JSONObject();
//        try {
////            jsonObject = (JSONObject) jsonParser.parse(department);
////            DepartmentEntity departmentEntity = (DepartmentEntity) jsonObject;
//
////            Object object = jsonParser.parse(department);
////            DepartmentEntity departmentEntity = (DepartmentEntity) object;
////            System.out.println(departmentEntity);
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }

        try {
            for (int i = 0; i < files.size(); i++) {
                String originName = files.get(i).getOriginalFilename();

                assert originName != null;
                File folder = new File(UPLOAD_PATH);

                if (!folder.exists()) folder.mkdirs();

                File newFile = new File(UPLOAD_PATH, originName);
                files.get(i).transferTo(newFile);
            }
        } catch (IOException e) {
            System.out.println(e);
            return false;
        }
        return true;
    }

}
