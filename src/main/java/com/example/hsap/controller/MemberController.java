package com.example.hsap.controller;

import com.example.hsap.dto.*;
import com.example.hsap.model.CategoryEntity;
import com.example.hsap.model.HistoryEntity;
import com.example.hsap.model.MemberEntity;
import com.example.hsap.security.TokenProvider;
import com.example.hsap.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/member")
public class MemberController {
    @Autowired
    MemberService memberService;

    @Autowired
    TokenProvider tokenProvider;

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

    @GetMapping("/histories")
    public ResponseEntity<?> getHistories(@AuthenticationPrincipal String memberId) {
        try {
            List<HistoryEntity> historyEntities = memberService.getHistories(memberId);
            List<HistoryDTO> historyDTOS = historyEntities.stream().map(HistoryDTO::new).toList();
            ResponseDTO response = ResponseDTO.<HistoryDTO>builder().data(historyDTOS).build();
            return ResponseEntity.ok().body(response);
        } catch (Exception ex) {
            ResponseDTO response = ResponseDTO.builder().error(ex.getMessage()).build();
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping("/categories")
    public ResponseEntity<?> getCategories(@AuthenticationPrincipal String memberId) {
        try {
            List<CategoryEntity> categories = memberService.getCategories(memberId);
            List<CategoryDTO> categoryDTOS = categories.stream().map(CategoryDTO::new).toList();
            ResponseDTO response = ResponseDTO.<CategoryDTO>builder().data(categoryDTOS).build();
            return ResponseEntity.ok().body(response);
        } catch (Exception ex) {
            ResponseDTO response = ResponseDTO.builder().error(ex.getMessage()).build();
            return ResponseEntity.badRequest().body(response);
        }
    }
}
@RestController
@RequestMapping("/auth")
class AuthController {
    @Autowired
    MemberService memberService;
    @Autowired
    TokenProvider tokenProvider;

    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @PostMapping("/signup")
    public ResponseEntity<?> registerMember(@RequestBody MemberDTO dto) {
        try {
            dto.addAuthority(new AuthorityDTO("ROLE_USER"));
            MemberEntity entity = MemberDTO.toEntity(dto);
            entity.setPassword(passwordEncoder.encode(dto.getPassword()));
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
                memberDTO.getPassword(),
                passwordEncoder);
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

    @GetMapping("/admin")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public String helloAdmin() {
        return "hello admin page";
    }

    @GetMapping("/leader")
    @PreAuthorize("hasAnyRole('ADMIN', 'LEADER')")
    public String helloLeader() {
        return "hello leader page";
    }

    @GetMapping("/user")
    @PreAuthorize("hasAnyRole('USER')")
    public String helloUser() {
        return "hello user page";
    }

}
