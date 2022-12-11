package com.example.hsap.controller;

import com.example.hsap.dto.PeopleDTO;
import com.example.hsap.dto.ResponseDTO;
import com.example.hsap.model.MemberEntity;
import com.example.hsap.model.PeopleEntity;
import com.example.hsap.security.MemberDetails;
import com.example.hsap.service.MemberService;
import com.example.hsap.service.PeopleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/people")
public class PeopleController {

    private final PeopleService peopleService;

    private final MemberService memberService;

    @PostMapping
    public ResponseEntity<?> create(
            @RequestBody PeopleDTO peopleDTO,
            @AuthenticationPrincipal MemberDetails principal) {
        try {
            MemberEntity member = memberService.searchById(principal.getUserId());
            PeopleEntity peopleEntity = PeopleDTO.toEntity(peopleDTO);
            peopleEntity.setDepartment(member.getDepartment());
            List<PeopleEntity> peopleEntityList = peopleService.create(peopleEntity);
            List<PeopleDTO> peopleDTOS = peopleEntityList.stream().map(PeopleDTO::new).toList();
            ResponseDTO<PeopleDTO> response = ResponseDTO.<PeopleDTO>builder().data(peopleDTOS).build();
            return ResponseEntity.ok().body(response);
        } catch (Exception e) {
            ResponseDTO response = ResponseDTO.builder().error(e.getMessage()).build();
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PutMapping
    public ResponseEntity<?> update(@RequestBody PeopleDTO peopleDTO) {
        try {
            PeopleEntity peopleEntity = PeopleDTO.toEntity(peopleDTO);
            List<PeopleEntity> peopleEntityList = peopleService.update(peopleEntity);
            List<PeopleDTO> peopleDTOS = peopleEntityList.stream().map(PeopleDTO::new).toList();
            ResponseDTO<PeopleDTO> response = ResponseDTO.<PeopleDTO>builder().data(peopleDTOS).build();
            return ResponseEntity.ok().body(response);
        } catch (Exception e) {
            ResponseDTO response = ResponseDTO.builder().error(e.getMessage()).build();
            return ResponseEntity.badRequest().body(response);
        }
    }

    @DeleteMapping
    public ResponseEntity<?> delete(@RequestBody PeopleDTO peopleDTO) {
        try {
            List<PeopleEntity> peopleEntities = peopleService.delete(peopleDTO.getId(), peopleDTO.getDepartment().getName());
            List<PeopleDTO> peopleDTOS = peopleEntities.stream().map(PeopleDTO::new).toList();
            ResponseDTO response = ResponseDTO.<PeopleDTO>builder().data(peopleDTOS).build();
            return ResponseEntity.ok().body(response);
        } catch (Exception e) {
            ResponseDTO response = ResponseDTO.builder().error(e.getMessage()).build();
            return ResponseEntity.badRequest().body(response);
        }
    }

}
