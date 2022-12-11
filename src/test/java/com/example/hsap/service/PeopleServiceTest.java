package com.example.hsap.service;

import com.example.hsap.model.DepartmentEntity;
import com.example.hsap.model.Gender;
import com.example.hsap.model.PeopleEntity;
import com.example.hsap.model.Status;
import com.example.hsap.repository.DepartmentRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@SpringBootTest
class PeopleServiceTest {

    @Autowired
    private PeopleService peopleService;

    @Autowired
    private DepartmentRepository departmentRepository;

    @Test
    @Transactional
    public void createTest() {
        DepartmentEntity department = departmentRepository.findByName("관리부");
        PeopleEntity peopleEntity = PeopleEntity.builder()
                .name("나성민")
                .birth("990201")
                .gender(Gender.MALE)
                .phoneNumber("01012345678")
                .status(Status.ACTIVE)
                .memo("메모")
                .department(department)
                .build();

        List<PeopleEntity> peopleEntityList = peopleService.create(peopleEntity);
        System.out.println(peopleEntityList);
    }

}