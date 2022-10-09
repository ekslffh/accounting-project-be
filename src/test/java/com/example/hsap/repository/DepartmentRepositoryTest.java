package com.example.hsap.repository;

import com.example.hsap.model.DepartmentEntity;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class DepartmentRepositoryTest {

    @Autowired
    private DepartmentRepository departmentRepository;

    @Test
    @DisplayName("existsByName 테스팅")
    public void existsByNameTest() {
        DepartmentEntity department1 = DepartmentEntity
                .builder()
                .name("A부서")
                .build();
        departmentRepository.save(department1);

        Assertions.assertTrue(departmentRepository.existsByName("A부서"));
        Assertions.assertFalse(departmentRepository.existsByName("B부서"));
    }

}