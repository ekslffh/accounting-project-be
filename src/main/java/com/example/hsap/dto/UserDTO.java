package com.example.hsap.dto;

import java.time.LocalDateTime;
import com.example.hsap.model.UserEntity;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Data;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class UserDTO {

    private String token;
    private String email;
    private String password;
    private String name;
    private String birth;
    private String gender;
    private LocalDateTime createdAt;

    public UserDTO(UserEntity userEntity) {
        this.email = userEntity.getEmail();
        this.name = userEntity.getName();
        this.birth = userEntity.getBirth();
        this.gender = userEntity.getGender();
        this.createdAt = userEntity.getCreatedAt();
    }

    public static UserEntity toEntity(UserDTO userDTO) {
        return UserEntity.builder()
                .email(userDTO.getEmail())
                .password(userDTO.getPassword())
                .name(userDTO.getName())
                .birth(userDTO.getBirth())
                .gender(userDTO.getGender())
                .createdAt(userDTO.getCreatedAt())
                .build();
    }

}
