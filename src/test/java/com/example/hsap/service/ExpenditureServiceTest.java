//package com.example.hsap.service;
//
//import com.example.hsap.model.CategoryEntity;
//import com.example.hsap.model.DepartmentEntity;
//import com.example.hsap.model.HistoryEntity;
//import com.example.hsap.model.MemberEntity;
//import com.example.hsap.repository.CategoryRepository;
//import com.example.hsap.repository.DepartmentRepository;
//import com.example.hsap.repository.HistoryRepository;
//import com.example.hsap.repository.MemberRepository;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import java.time.LocalDateTime;
//
//@SpringBootTest
//class ExpenditureServiceTest {
//
//    @Autowired
//    ExpenditureService expenditureService;
//    @Autowired
//    HistoryRepository expenditureRepository;
//    @Autowired
//    MemberRepository memberRepository;
//    @Autowired
//    DepartmentRepository departmentRepository;
//    @Autowired
//    CategoryRepository categoryRepository;
//
//    DepartmentEntity departmentEntity;
//    MemberEntity memberEntity;
//    CategoryEntity categoryEntity;
//
//    // 사전처리 : temp member, department, category 생성하기
//    @BeforeEach
//    public void before() {
//
//        // 임시 부서
//        departmentEntity = DepartmentEntity.builder()
//                .name("임시부서")
//                .asset(0)
//                .build();
//        departmentRepository.save(departmentEntity);
//
//        // 임시 사용자
//        memberEntity = MemberEntity.builder()
//                .department(departmentEntity)
//                .email("temp@naver.com")
//                .password("1234")
//                .name("temporary-user")
//                .birth("990201")
//                .gender("male")
//                .asset(0)
//                .joinedAt(LocalDateTime.now())
//                .build();
//        memberRepository.save(memberEntity);
//
//        // 임시 카테고리
//        categoryEntity = CategoryEntity.builder()
//                .title("미정")
//                .description("임시 카테고리입니다.")
//                .department(memberEntity.getDepartment().getName())
//                .build();
//        categoryRepository.save(categoryEntity);
//    }
//
//    public void printInfo() {
//        departmentRepository.findAll().forEach(d -> {
//            System.out.println("### " + d.getName() + " ###");
//            System.out.println("*** 소속 멤버들 ***");
//            d.getMembers().forEach(System.out::println);
//            System.out.println("*** 소속 내역들 ***");
//            d.getHistories().forEach(System.out::println);
//            System.out.println("*** 소속 카테고리들 ***");
//            categoryRepository.findByDepartment(d.getName()).forEach(System.out::println);
//        });
//        System.out.println();
//
//        System.out.println("### members ###");
//        memberRepository.findAll().forEach(m -> {
//            System.out.println(m.getName());
//            m.getExpenditures().forEach(System.out::println);
//        });
//        System.out.println();
//
//        System.out.println("### categories ###");
//        categoryRepository.findAll().forEach(c -> {
//            System.out.println(c.getTitle());
//            c.getHistories().forEach(System.out::println);
//        });
//    }
//
//    @Test
//    public void test() {
//        printInfo();
//    }
//
//    @Test
//    public void createTest() {
//        HistoryEntity expend1 = HistoryEntity.builder()
//                .useDate(LocalDateTime.now().minusDays(2L))
//                .categoryId(categoryEntity.getId())
//                .inOrOut("지출")
//                .amountOfMoney(50000)
//                .memo("소그룹 리트릿")
//                .member(memberEntity)
//                .departmentId(memberEntity.getDepartment().getId())
//                .build();
//
////        expenditureRepository.save(expend1);
//        expenditureService.create(expend1);
//        printInfo();
//
////        expenditureService.delete(expend1);
////
////        printInfo();
//
////        expend1.setMemo("업데이트");
////        expenditureService.update(expend1);
////        printInfo();
//    }
//
//    // create :
//
//}