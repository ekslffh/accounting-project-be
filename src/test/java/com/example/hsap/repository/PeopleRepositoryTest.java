package com.example.hsap.repository;

import com.example.hsap.model.DepartmentEntity;
import com.example.hsap.model.Gender;
import com.example.hsap.model.PeopleEntity;
import com.example.hsap.model.Status;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
class PeopleRepositoryTest {

    @Autowired
    private PeopleRepository peopleRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    @Test
    @Transactional
    void relationTest() {
        DepartmentEntity department = departmentRepository.findByName("관리부");
        System.out.println(department);

        PeopleEntity peopleEntity = PeopleEntity.builder()
                .name("나성민")
                .birth("990201")
                .gender(Gender.MALE)
                .phoneNumber("01012345678")
                .status(Status.ACTIVE)
                .memo("메모")
                .department(department)
                .build();

        peopleRepository.saveAndFlush(peopleEntity);

        System.out.println(department.getPeoples());
    }

}