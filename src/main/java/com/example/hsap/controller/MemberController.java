package com.example.hsap.controller;

import com.example.hsap.dto.*;
import com.example.hsap.model.CategoryEntity;
import com.example.hsap.model.HistoryEntity;
import com.example.hsap.model.MemberEntity;
import com.example.hsap.security.MemberDetails;
import com.example.hsap.security.TokenProvider;
import com.example.hsap.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.nurigo.sdk.NurigoApp;
import net.nurigo.sdk.message.exception.NurigoMessageNotReceivedException;
import net.nurigo.sdk.message.model.Message;
import net.nurigo.sdk.message.service.DefaultMessageService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @GetMapping
    public ResponseEntity<?> retrieveAll() {
        List<MemberEntity> entities = memberService.retrieveAll();
        List<MemberDTO> dtos = entities.stream().map(MemberDTO::new).toList();
        ResponseDTO response = ResponseDTO.<MemberDTO>builder().data(dtos).build();
        return ResponseEntity.ok().body(entities);
    }

    @GetMapping("/deleted")
    public ResponseEntity<?> retrieveDeletedByDepartment(@RequestParam String name) {
        try {
            List<MemberEntity> entities = memberService.retrieveDeletedByDepartment(name);
            List<MemberDTO> dtos = entities.stream().map(MemberDTO::new).toList();
            ResponseDTO response = ResponseDTO.<MemberDTO>builder().data(dtos).build();
            return ResponseEntity.ok().body(response);
        } catch (Exception ex) {
            ResponseDTO response = ResponseDTO.builder().error(ex.getMessage()).build();
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PutMapping("/recover")
    public ResponseEntity<?> recover(@RequestBody MemberDTO memberDTO) {
        try {
            List<MemberEntity> entities = memberService.recover(memberDTO.getId());
            List<MemberDTO> dtos = entities.stream().map(MemberDTO::new).toList();
            ResponseDTO response = ResponseDTO.<MemberDTO>builder().data(dtos).build();
            return ResponseEntity.ok().body(response);
        } catch (Exception ex) {
            ResponseDTO response = ResponseDTO.builder().error(ex.getMessage()).build();
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PutMapping
    public ResponseEntity<?> update(@RequestBody MemberDTO memberDTO) {
        try {
            MemberEntity member = MemberDTO.toEntity(memberDTO);
            MemberEntity entity = memberService.update(member);
            MemberDTO dto = new MemberDTO(entity);
            return ResponseEntity.ok().body(dto);
        } catch (Exception ex) {
            ResponseDTO response = ResponseDTO.builder().error(ex.getMessage()).build();
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PutMapping("/password")
    public ResponseEntity<?> updatePassword(@RequestBody MemberDTO memberDTO, @AuthenticationPrincipal MemberDetails principal) {
        try {
            MemberEntity member = memberService.searchById(principal.getUserId());
            member.setPassword(passwordEncoder.encode(memberDTO.getPassword()));
            MemberEntity memberEntity = memberService.updatePassword(member);
            MemberDTO response = new MemberDTO(memberEntity);
            return ResponseEntity.ok().body(response);
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @DeleteMapping
    @PreAuthorize("hasAnyRole('LEADER')")
    public ResponseEntity<?> delete(@RequestBody MemberDTO memberDTO) {
        try {
            MemberEntity entity = MemberEntity.builder().email(memberDTO.getEmail()).build();
            memberService.delete(entity);
            return ResponseEntity.ok().body(memberDTO);
        } catch (Exception ex) {
            ResponseDTO response = ResponseDTO.builder().error(ex.getMessage()).build();
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping("/histories")
    public ResponseEntity<?> getHistories(@AuthenticationPrincipal MemberDetails principal, @RequestParam(required = false) String year) {
        try {
            List<HistoryEntity> historyEntities = memberService.getHistories(principal.getUserId(), year);
            List<HistoryDTO> historyDTOS = historyEntities.stream().map(HistoryDTO::new).toList();
            ResponseDTO response = ResponseDTO.<HistoryDTO>builder().data(historyDTOS).build();
            return ResponseEntity.ok().body(response);
        } catch (Exception ex) {
            ResponseDTO response = ResponseDTO.builder().error(ex.getMessage()).build();
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping("/categories")
    public ResponseEntity<?> getCategories(@AuthenticationPrincipal MemberDetails principal) {
        try {
            List<CategoryEntity> categories = memberService.getCategories(principal.getUserId());
            List<CategoryDTO> categoryDTOS = categories.stream().map(CategoryDTO::new).toList();
            ResponseDTO response = ResponseDTO.<CategoryDTO>builder().data(categoryDTOS).build();
            return ResponseEntity.ok().body(response);
        } catch (Exception ex) {
            ResponseDTO response = ResponseDTO.builder().error(ex.getMessage()).build();
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping("/principal")
    public ResponseEntity<?> getMemberPrincipal(@AuthenticationPrincipal MemberDetails principal) {
        try {
            MemberEntity member = memberService.searchById(principal.getUserId());
            MemberDTO memberDTO = new MemberDTO(member);
            return ResponseEntity.ok().body(memberDTO);
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }
}

@RestController
@Slf4j
@RequestMapping("/auth")
@RequiredArgsConstructor
class AuthController {
    private final MemberService memberService;
    private final TokenProvider tokenProvider;
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private final DefaultMessageService messageService = NurigoApp.INSTANCE.initialize("NCS3CDJQNKS6YNXZ", "AO8BIRTEPZLKY2CMRBZ8VCY0RDPT4ASM", "https://api.solapi.com");

    @PostMapping("/phone")
    public ResponseEntity<?> phoneAuthenticate(@RequestBody MessageInfo messageInfo) {
        log.info("receiver 전화번호: ", messageInfo.getReceiver());
        Message message = new Message();
        // 발신번호 및 수신번호는 반드시 01012345678 형태로 입력되어야 합니다.
        message.setFrom("01084819926");
        message.setTo(messageInfo.getReceiver());
        // 랜덤 4자리 수 전송
        String authNumber = MessageInfo.numberGen(4, 1);
        message.setText("HSAP 인증번호 [" + authNumber + "]");
        try {
            this.messageService.send(message);
        } catch (NurigoMessageNotReceivedException exception) {
            ResponseDTO response = ResponseDTO.builder().error(exception.getMessage()).build();
            log.error("failed message list: " + exception.getFailedMessageList().toString());
            return ResponseEntity.badRequest().body(response);
        } catch (Exception exception) {
            ResponseDTO response = ResponseDTO.builder().error(exception.getMessage()).build();
            return ResponseEntity.badRequest().body(response);
        }
        return ResponseEntity.ok().body(authNumber);
    }

    // 중복: true, 중복아님: false
    @GetMapping("/emailcheck")
    public boolean checkDuplicateEmail(@RequestParam String email) {
        if (email == null || email.equals("")) return true;
        // 중복되지 않았을 때
        return memberService.checkDuplicateEmail(email);
    }

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
        // 인증 성공
        if (member != null) {
            final String token = tokenProvider.create(member);
            final MemberDTO responseMemberDTO = new MemberDTO(member);
            responseMemberDTO.setToken(token);
            return ResponseEntity.ok().body(responseMemberDTO);
        } else {
            ResponseDTO response = ResponseDTO.builder().error("로그인 인증 실패").build();
            return ResponseEntity.badRequest().body(response);
        }
    }

}
