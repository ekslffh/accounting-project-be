package com.example.hsap.dto;

import com.example.hsap.model.Gender;
import com.example.hsap.model.PeopleEntity;
import com.example.hsap.model.Status;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Data;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class PeopleDTO {

    private String id;

    private DepartmentDTO department;

    private String name;

    private String birth;

    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    private Status status;

    private String memo;

    public PeopleDTO(PeopleEntity peopleEntity) {
        this.id = peopleEntity.getId();
        this.name = peopleEntity.getName();
        this.birth = peopleEntity.getBirth();
        this.phoneNumber = peopleEntity.getPhoneNumber();
        this.gender = peopleEntity.getGender();
        this.status = peopleEntity.getStatus();
        this.memo = peopleEntity.getMemo();
        this.department = new DepartmentDTO(peopleEntity.getDepartment());
    }

    public static PeopleEntity toEntity(PeopleDTO peopleDTO) {
        return PeopleEntity.builder()
                .id(peopleDTO.getId())
                .name(peopleDTO.getName())
                .birth(peopleDTO.getBirth())
                .phoneNumber(peopleDTO.getPhoneNumber())
                .gender(peopleDTO.getGender())
                .status(peopleDTO.getStatus())
                .memo(peopleDTO.getMemo())
                .build();
    }

}
