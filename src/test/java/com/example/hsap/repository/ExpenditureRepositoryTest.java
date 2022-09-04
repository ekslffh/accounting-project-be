//package com.example.hsap.repository;
//
//import com.example.hsap.model.CategoryEntity;
//import com.example.hsap.model.DepartmentEntity;
//import com.example.hsap.model.ExpenditureEntity;
//import com.example.hsap.model.MemberEntity;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import javax.transaction.Transactional;
//import java.time.LocalDateTime;
//import java.util.List;
//
//@SpringBootTest
//class ExpenditureRepositoryTest {
//
//    @Autowired
//    private ExpenditureRepository expenditureRepository;
//    @Autowired
//    private MemberRepository memberRepository;
//
//    @Autowired
//    private DepartmentRepository departmentRepository;
//
//    @Autowired
//    private CategoryRepository categoryRepository;
//
//    MemberEntity member1;
//    MemberEntity member2;
//
//    DepartmentEntity department1;
//    DepartmentEntity department2;
//
//    @Test
//    public void oneToManyTest() {
//        System.out.println("===== member1 =====");
////        List<ExpenditureEntity> result1 = expenditureRepository.findByUserId(userRepository.findByEmail("hong@naver.com").getId());
//        List<ExpenditureEntity> result1 = memberRepository.findByEmail("hong@naver.com").getExpenditureEntities();
//        result1.forEach(System.out::println);
//        System.out.println("===== member2 =====");
////        List<ExpenditureEntity> result2 = expenditureRepository.findByUserId(userRepository.findByEmail("Lee@naver.com").getId());
//        List<ExpenditureEntity> result2 = memberRepository.findByEmail("Lee@naver.com").getExpenditureEntities();
//        result2.forEach(System.out::println);
//
//        System.out.println("=== member findAll ===");
//        memberRepository.findAll().forEach(System.out::println);
//        System.out.println("=== expenditure findAll ===");
//        expenditureRepository.findAll().forEach(System.out::println);
//    }
//
//    @BeforeEach
//    public void before() {
//        // 부서 저장 (청년부, 중고등부)
//        department1 = DepartmentEntity.builder()
//                .name("청년부")
//                .build();
//        department2 = DepartmentEntity.builder()
//                .name("중고등부")
//                .build();
//
//        departmentRepository.save(department1);
//        departmentRepository.save(department2);
//
//        // 유저 저장 (홍길동, 전우치)
//        member1 = MemberEntity.builder()
//                .departmentEntity(department2)
//                .email("hong@naver.com")
//                .password("1234")
//                .name("홍길동")
//                .build();
//        member2 = MemberEntity.builder()
//                .departmentEntity(department1)
//                .email("Lee@naver.com")
//                .password("1234")
//                .name("이순신")
//                .build();
//        memberRepository.save(member1);
//        memberRepository.save(member2);
//
//        // 사용내역 저장 (지출1, 지출2, 지출3)
//        ExpenditureEntity expend1 = ExpenditureEntity.builder()
//                .useDate(LocalDateTime.now())
//                .departmentId(member2.getDepartmentEntity().getId())
//                .category("소그룹회식")
//                .inOrOut("지출")
//                .amountOfMoney(80000)
//                .member(member2)
//                .memo("홍길동팀")
//                .build();
//        ExpenditureEntity expend2 = ExpenditureEntity.builder()
//                .useDate(LocalDateTime.now().minusDays(9))
//                .departmentId(member1.getDepartmentEntity().getId())
//                .category("수련회")
//                .inOrOut("지출")
//                .amountOfMoney(100000)
//                .member(member1)
//                .memo("여름수련회")
//                .build();
//        ExpenditureEntity expend3 = ExpenditureEntity.builder()
//                .useDate(LocalDateTime.now().minusDays(3))
//                .departmentId(member2.getDepartmentEntity().getId())
//                .category("소그룹리트릿")
//                .inOrOut("지출")
//                .amountOfMoney(50000)
//                .member(member2)
//                .memo("홍길동팀")
//                .build();
//        expenditureRepository.save(expend1);
//        expenditureRepository.save(expend2);
//        expenditureRepository.save(expend3);
//
//        // 카테고리 저장 (소그룹활동1, 소그룹활동2)
//        CategoryEntity category1 = CategoryEntity.builder()
//                .title("소그룹활동")
//                .description("소그룹활동입니다.")
//                .department(member1.getDepartmentEntity().getName())
//                .build();
//        CategoryEntity category2 = CategoryEntity.builder()
//                .title("소그룹활동2")
//                .description("소그룹활동입니다.")
//                .department(member2.getDepartmentEntity().getName())
//                .build();
//
//        categoryRepository.save(category2);
//        categoryRepository.save(category1);
//    }
//
//    @Test
//    @Transactional
//    public void departmentTest() {
//        List<DepartmentEntity> result = departmentRepository.findAll();
//        result.forEach(r -> {
//            System.out.println("===== " + r.getName() + " =====");
//            r.getExpenditureEntities().forEach(System.out::println);
//        });
//    }
//
//}