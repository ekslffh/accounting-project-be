package com.example.hsap.controller;

import com.example.hsap.dto.HistoryDTO;
import com.example.hsap.dto.MemberDTO;
import com.example.hsap.dto.ResponseDTO;
import com.example.hsap.model.HistoryEntity;
import com.example.hsap.model.MemberEntity;
import com.example.hsap.security.TokenProvider;
import com.example.hsap.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/auth")
public class MemberController {
    @Autowired
    MemberService memberService;

    @Autowired
    TokenProvider tokenProvider;

    @PostMapping("/signup")
    public ResponseEntity<?> registerMember(@RequestBody MemberDTO dto) {
        try {
            MemberEntity entity = MemberDTO.toEntity(dto);
            MemberEntity savedEntity = memberService.create(entity);
            MemberDTO responseDTO = new MemberDTO(savedEntity);
            return ResponseEntity.ok().body(responseDTO);
        } catch (Exception ex) {
            ResponseDTO response = ResponseDTO.builder().error(ex.getMessage()).build();
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PostMapping("/signin")
    public ResponseEntity<?> authenticate(@RequestBody MemberDTO memberDTO) {

        MemberEntity member = memberService.getByCredentials(
                memberDTO.getEmail(),
                memberDTO.getPassword());

        if (member != null) {
            final String token = tokenProvider.create(member);
            final MemberDTO responseMemberDTO = new MemberDTO(member);
            responseMemberDTO.setToken(token);
            return ResponseEntity.ok().body(responseMemberDTO);
        } else {
            ResponseDTO response = ResponseDTO.builder().error("Login failed").build();
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping
    public ResponseEntity<?> retrieveAll() {
        List<MemberEntity> entities = memberService.retrieveAll();
        List<MemberDTO> dtos = entities.stream().map(MemberDTO::new).toList();
        ResponseDTO response = ResponseDTO.<MemberDTO>builder().data(dtos).build();
        return ResponseEntity.ok().body(entities);
    }

//    @PutMapping
//    public ResponseEntity<?> update(@RequestBody MemberDTO memberDTO) {
//        try {
//            MemberEntity member = MemberDTO.toEntity(memberDTO);
//            MemberEntity entity = memberService.update(member);
//            MemberDTO dto = new MemberDTO(entity);
//            return ResponseEntity.ok().body(dto);
//        } catch (Exception ex) {
//            ResponseDTO response = ResponseDTO.builder().error(ex.getMessage()).build();
//            return ResponseEntity.badRequest().body(response);
//        }
//    }

    @GetMapping("/expenditures")
    public ResponseEntity<?> getExpenditures(@RequestBody MemberDTO memberDTO) {
        try {
            MemberEntity memberEntity = MemberDTO.toEntity(memberDTO);
            List<HistoryEntity> expenditureEntities = memberService.getExpenditures(memberEntity);
            List<HistoryDTO> expenditureDTOS = expenditureEntities.stream().map(HistoryDTO::new).toList();
            ResponseDTO response = ResponseDTO.<HistoryDTO>builder().data(expenditureDTOS).build();
            return ResponseEntity.ok().body(response);
        } catch (Exception ex) {
            ResponseDTO response = ResponseDTO.builder().error(ex.getMessage()).build();
            return ResponseEntity.badRequest().body(response);
        }
    }
}
