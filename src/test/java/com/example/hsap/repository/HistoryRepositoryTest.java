package com.example.hsap.repository;

import com.example.hsap.model.CategoryEntity;
import com.example.hsap.model.DepartmentEntity;
import com.example.hsap.model.HistoryEntity;
import com.example.hsap.model.MemberEntity;
import com.example.hsap.service.HistoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.time.LocalDateTime;
import java.util.List;

@SpringBootTest
class HistoryRepositoryTest {

    @Autowired
    DepartmentRepository departmentRepository;

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    HistoryService historyService;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    HistoryRepository historyRepository;

    @BeforeEach
    public void before() {
        DepartmentEntity department = departmentRepository.findByName("청년부");
        DepartmentEntity department1 = departmentRepository.findByName("중고등부");
        MemberEntity member = memberRepository.findByEmail("youth");
        CategoryEntity category = CategoryEntity.builder().title("카테고리1").build();
        category.setDepartment(department);
        categoryRepository.save(category);
        CategoryEntity categoryEntity = categoryRepository.findAll().stream().findFirst().get();

        HistoryEntity historyEntity = HistoryEntity.builder().category(categoryEntity).useDate(LocalDateTime.of(2021, 12, 31, 23, 59, 59)).department(member.getDepartment()).member(member).build();
        HistoryEntity historyEntity2 = HistoryEntity.builder().category(categoryEntity).useDate(LocalDateTime.of(2021, 4, 5, 4, 3)).department(department).member(member).build();
        HistoryEntity historyEntity3 = HistoryEntity.builder().category(categoryEntity).useDate(LocalDateTime.of(2022, 12, 3, 4,5)).department(member.getDepartment()).member(member).build();

//        historyService.create(historyEntity);
//        historyService.create(historyEntity2);
//        historyService.create(historyEntity3);
    }

    @Test
    public void yearTest() {
        DepartmentEntity department = departmentRepository.findByName("청년부");
        List<HistoryEntity> histories = historyRepository.findAll();
        histories.forEach(history -> {
            System.out.println(history.getUseDate());
        });
        System.out.println("===================================================");
        List<HistoryEntity> filteredHistories = histories.stream().filter(history -> history.getUseDate().getYear() == 2021).toList();
        filteredHistories.forEach(historyEntity -> {
            System.out.println(historyEntity.getUseDate());
        });
    }

}