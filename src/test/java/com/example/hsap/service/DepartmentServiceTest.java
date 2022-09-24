package com.example.hsap.service;

import com.example.hsap.model.DepartmentEntity;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class DepartmentServiceTest {

    @Autowired
    private DepartmentService departmentService;

    private void givenDepartment(String name) {
        DepartmentEntity department = DepartmentEntity.builder().name(name).build();
        departmentService.create(department);
    }

    @Test
    void createTest() {
        // 1. 생성 테스트
        // 1-1. 부서 하나 생성 후 전체 가져오면 사이즈는 1이다.
        givenDepartment("청년부");
        Assertions.assertEquals(1, departmentService.retrieveAll().size());
        // 1-2. createdAt, updatedAt 자동으로 들어간다.
        DepartmentEntity department = departmentService.retrieveAll().get(0);
        Assertions.assertNotNull(department.getCreatedAt());
        Assertions.assertNotNull(department.getUpdatedAt());
//        System.out.println(">>> department : " + department);

        // 2. 예외 테스트
        // 2-1. 이름이 들어가 있지 않은 경우 Exception 발생
//        try {
//            givenDepartment(null);
//        } catch (Exception e) {
//            System.out.println(">>> 2-1 : " + e.getMessage());
//        }
        Assertions.assertThrows(RuntimeException.class, () -> {
            givenDepartment(null);
        });

        // 2-2. 중복해서 만드는 경우 Exception 발생
//        try {
//            givenDepartment("청년부");
//        } catch (Exception e) {
//            System.out.println(">>> 2-2 : " + e.getMessage());
//        }
        Assertions.assertThrows(RuntimeException.class, () -> {
            givenDepartment("청년부");
        });
    }

    @Test
    void retrieveTest() {
        // 1. 전체 조회 테스트
        Assertions.assertEquals(0, departmentService.retrieveAll().size());
        givenDepartment("청년부");
        givenDepartment("중고등부");
        Assertions.assertEquals(2, departmentService.retrieveAll().size());
        // 2. 특정 부서 조회 테스트
        DepartmentEntity department = departmentService.retrieve("청년부");
        Assertions.assertNotNull(department);
        Assertions.assertNull(departmentService.retrieve("없는 부서"));
    }

    @Test
    void updateTest() {
        // 1. 특정 부서 이름 수정 테스트
        givenDepartment("청년부");
        DepartmentEntity department = departmentService.retrieve("청년부");
        department.setName("청년부-수정");
        departmentService.update(department);
        Assertions.assertEquals("청년부-수정",departmentService.retrieveAll().get(0).getName());

        // 2. 예외 테스트
        // 2-1. 존재하지 않는 아이디일 경우 Exception 발생
        department.setId("1234");
        try {
            departmentService.update(department);
        } catch (Exception e) {
            System.out.println(">>> 2-1 : " + e.getMessage());
        }
        Assertions.assertThrows(RuntimeException.class, () -> {
            departmentService.update(department);
        });
    }

    @Test
    void deleteTest() {
        // 1. 삭제 테스트
        givenDepartment("청년부");
        givenDepartment("중고등부");
        Assertions.assertEquals(2, departmentService.retrieveAll().size());

        DepartmentEntity department1 = departmentService.retrieve("청년부");
        departmentService.delete(department1);
        Assertions.assertEquals(1, departmentService.retrieveAll().size());

        // 2. 예외 테스트
        // 2-1. 부서가 비어있는 경우
        Assertions.assertThrows(RuntimeException.class, () -> {
            departmentService.delete(null);
        });

        // 2-2. 존재하지 않는 부서 일 경우 (에러는 나지 않음.)
        department1.setId("1234");
        departmentService.delete(department1);
        Assertions.assertEquals(1, departmentService.retrieveAll().size());
    }
}