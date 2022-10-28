package com.example.hsap.dto;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import com.example.hsap.model.AuthorityEntity;
import com.example.hsap.model.MemberEntity;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.stereotype.Component;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Component
public class MemberDTO {
    private String id;

    private String token;
    private String email;

    private String password;
    private String name;

    private String birth;
    private String phoneNumber;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private DepartmentDTO department;
    private Set<AuthorityDTO> authority = new HashSet<>();
    public void addAuthority(AuthorityDTO authorityDTO) {
        authority.add(authorityDTO);
    }

    public MemberDTO(MemberEntity memberEntity) {
        this.id = memberEntity.getId();
        this.department = new DepartmentDTO(memberEntity.getDepartment());
        this.email = memberEntity.getEmail();
        this.name = memberEntity.getName();
        this.birth = memberEntity.getBirth();
        this.phoneNumber = memberEntity.getPhoneNumber();
//        this.gender = memberEntity.getGender();
//        this.asset = memberEntity.getAsset();
        this.createdAt = memberEntity.getCreatedAt();
        this.updatedAt = memberEntity.getUpdatedAt();
        this.authority = memberEntity.getAuthorities().stream().map(
                authority -> new AuthorityDTO(authority.getAuthorityName())
        ).collect(Collectors.toSet());
    }

    public static MemberEntity toEntity(MemberDTO memberDTO) {
        MemberEntity memberEntity = new MemberEntity();
        memberEntity.setId(memberDTO.getId());
        memberEntity.setDepartment(DepartmentDTO.toEntity(memberDTO.getDepartment()));
        memberEntity.setEmail(memberDTO.getEmail());
        memberEntity.setPassword(memberDTO.getPassword());
        memberEntity.setName(memberDTO.getName());
        memberEntity.setBirth(memberDTO.getBirth());
        memberEntity.setPhoneNumber(memberDTO.getPhoneNumber());
        memberEntity.setAuthorities(memberDTO.getAuthority().stream().map(
                authorityDTO -> new AuthorityEntity(authorityDTO.getAuthorityName()))
                .collect(Collectors.toSet()));
        return memberEntity;
    }

}
