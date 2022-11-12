//package com.example.hsap.service;
//
//import com.example.hsap.model.CategoryEntity;
//import com.example.hsap.model.DepartmentEntity;
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import java.util.List;
//
//@SpringBootTest
//class CategoryServiceTest {
//    @Autowired
//    CategoryService categoryService;
//    @Autowired
//    DepartmentService departmentService;
//
//    private List<CategoryEntity> givenCategory(String title, String description, DepartmentEntity department) {
//        CategoryEntity category = CategoryEntity.builder()
//                .title(title)
//                .description(description)
//                .department(department)
//                .build();
//        return categoryService.create(category);
//    }
//
//    @Test
//    void createTest() {
//        // 1. 생성 테스트
//        // 1-1. 두개의 카테고리를 특정 부서에 저장하면 특정 부서에서 카테고리들 조회가능
//        DepartmentEntity department = new DepartmentEntity();
//        department.setName("청년부");
//        givenCategory("전체식사", "청년부 전체식사", department);
//        givenCategory("리트릿", "청년부 리트릿", department);
//        categoryService.retrieveByDepartment("청년부");
//        // 1-2. 동일한 이름의 카테고리 다른 부서에 생성가능
//        department.setName("중고등부");
//        givenCategory("전체식사", "중고등부 전체식사", department);
//        categoryService.retrieveByDepartment("중고등부");
//
//        // 2. 예외 테스트
//        // 2-1. 해당 부서가 없는 경우에 에러 발생
//        department.setName("없는 부서");
//        Assertions.assertThrows(RuntimeException.class, () -> {
//            givenCategory("전체식사", "없는 부서 전체식사", department);
//        });
//    }
//
//    @Test
//    void retrieveTest() {
//        // 1. 조회 테스트
//        DepartmentEntity department = new DepartmentEntity();
//        department.setName("청년부");
//        givenCategory("전체식사", "청년부 전체식사", department);
//
//        categoryService.retrieveByDepartment("청년부").forEach(System.out::println);
//        Assertions.assertEquals(1, categoryService.retrieveByDepartment("청년부").size());
//    }
//
//    @Test
//    void updateTest() {
//        // 1. 업데이트 테스트
//        // 1-1. 업데이트 시 카테고리와 연관관계를 맺고 있는 부서에서도 수정이 완료 되어야 한다.
//        DepartmentEntity department = new DepartmentEntity();
//        department.setName("청년부");
//        givenCategory("전체식사", "청년부 전체식사", department);
//        givenCategory("리트릿", "청년부 리트릿", department);
//
//        categoryService.retrieveByDepartment("청년부").forEach(System.out::println);
//
//        CategoryEntity category = categoryService.retrieveByDepartment("청년부").get(0);
//        category.setTitle("전체식사-수정입니다");
//        categoryService.update(category);
//
//        categoryService.retrieveByDepartment("청년부").forEach(System.out::println);
//    }
//
//    @Test
//    void deleteTest() {
//        // 1. 삭제 테스트
//        // 1-1. 삭제 시 해당 부서에서도 삭제가 되어야 한다.
//        DepartmentEntity department = new DepartmentEntity();
//        department.setName("청년부");
//        givenCategory("전체식사", "청년부 전체식사", department);
//        givenCategory("리트릿", "청년부 리트릿", department);
//
//        categoryService.retrieveByDepartment("청년부").forEach(System.out::println);
//        CategoryEntity category = categoryService.retrieveByDepartment("청년부").get(0);
//        categoryService.delete(category).forEach(System.out::println);
////        categoryService.retrieveByDepartment("청년부").forEach(System.out::println);
//        Assertions.assertEquals(1, categoryService.retrieveByDepartment("청년부").size());
//    }
//
//}