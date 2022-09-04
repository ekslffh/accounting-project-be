package com.example.hsap.dto;

import java.time.LocalDateTime;
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
    private String gender;
    private int asset;
    private LocalDateTime joinedAt;
    private DepartmentDTO department;

    public MemberDTO(MemberEntity memberEntity) {
        this.id = memberEntity.getId();
        this.department = new DepartmentDTO(memberEntity.getDepartment());
        this.email = memberEntity.getEmail();
        this.name = memberEntity.getName();
        this.birth = memberEntity.getBirth();
        this.gender = memberEntity.getGender();
        this.asset = memberEntity.getAsset();
        this.joinedAt = memberEntity.getJoinedAt();
    }
    public static MemberEntity toEntity(MemberDTO memberDTO) {
        return MemberEntity.builder()
                .id(memberDTO.getId())
                .department(DepartmentDTO.toEntity(memberDTO.getDepartment()))
                .email(memberDTO.getEmail())
                .password(memberDTO.getPassword())
                .name(memberDTO.getName())
                .birth(memberDTO.getBirth())
                .gender(memberDTO.getGender())
                .asset(memberDTO.getAsset())
                .joinedAt(memberDTO.getJoinedAt())
                .build();
    }

}
