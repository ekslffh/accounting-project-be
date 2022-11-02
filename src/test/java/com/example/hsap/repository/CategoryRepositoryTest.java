package com.example.hsap.repository;

import com.example.hsap.model.CategoryEntity;
import com.example.hsap.model.DepartmentEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class CategoryRepositoryTest {

    @Autowired
    private DepartmentRepository departmentRepository;
    @Autowired
    private CategoryRepository categoryRepository;

    @BeforeEach
    public void before() {
        DepartmentEntity department = departmentRepository.findById("0").orElseThrow();
        CategoryEntity category1 = CategoryEntity.builder().department(department).title("리트릿").description("3분기 리트릿").build();
        CategoryEntity category2 = CategoryEntity.builder().department(department).title("전체식사").description("3분기 전체식사").deleted(true).build();
        CategoryEntity category3 = CategoryEntity.builder().department(department).title("동아리").description("2분기 동아리").build();

        categoryRepository.save(category1);
        categoryRepository.save(category2);
        categoryRepository.save(category3);
    }

    @Test
    public void deletedCategoryTest() {
        System.out.println("findAll()");
        categoryRepository.findAll().forEach(System.out::println);
        System.out.println("custom findAll()");
//        categoryRepository.findDeletedCategory().forEach(System.out::println);
//
        DepartmentEntity department = departmentRepository.findById("0").orElseThrow();
//        categoryRepository.findByDepartment(department).forEach(System.out::println);

        System.out.println("==================================");
        categoryRepository.findDeletedCategoriesByDepartment(department.getId()).forEach(System.out::println);
    }

}