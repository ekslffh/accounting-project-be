//package com.example.hsap.repository;
//
//import com.example.hsap.model.DepartmentEntity;
//import com.example.hsap.model.MemberEntity;
//import org.assertj.core.util.Lists;
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import java.util.List;
//import java.util.Optional;
//
//@SpringBootTest
//class MemberRepositoryTest {
//    @Autowired
//    DepartmentRepository departmentRepository;
//    @Autowired
//    MemberRepository memberRepository;
//
//    @BeforeEach
//    void before() {
//        DepartmentEntity department = departmentRepository.findByName("청년부");
//        DepartmentEntity department1 = departmentRepository.findByName("중고등부");
//        MemberEntity member1 = MemberEntity.builder().department(department).phoneNumber("01084819926").name("유저1").email("user1").password("1234").build();
//        MemberEntity member2 = MemberEntity.builder().department(department).name("유저2").email("user2").password("1234").deleted(true).build();
//        MemberEntity member3 = MemberEntity.builder().department(department).name("유저3").email("user3").password("1234").build();
//        MemberEntity member4 = MemberEntity.builder().department(department1).name("유저4").email("user4").password("1234").deleted(true).build();
//        MemberEntity member5 = MemberEntity.builder().department(department).name("유저5").email("user5").password("1234").deleted(true).build();
//
//        memberRepository.saveAll(Lists.newArrayList(member1, member2, member3, member4, member5));
//    }
//
//    @Test
//    void recoverTest() {
//        System.out.println("존재하는 유저");
//        memberRepository.findAll().forEach(m -> System.out.println(m.getName()));
//        System.out.println("삭제된 유저");
//        List<MemberEntity> deletedMembers = memberRepository.findDeletedMembersByDepartment(departmentRepository.findByName("청년부").getId());
//        Assertions.assertEquals(2, deletedMembers.size());
//        deletedMembers.forEach(d -> System.out.println(d.getName()));
//        MemberEntity deleteMember = deletedMembers.get(0);
//        System.out.println(deleteMember.getName());
//        System.out.println("기존의 findById로 유저 찾아보기");
//        Optional<MemberEntity> findDeletedMember1 = memberRepository.findById(deleteMember.getId());
//        Assertions.assertTrue(findDeletedMember1.isEmpty());
////        System.out.println(findDeletedMember1.getName());
//        System.out.println("custom findById로 유저 찾아보기");
//        MemberEntity findDeletedMember2 = memberRepository.findByIdCustom(deleteMember.getId());
//        Assertions.assertNotNull(findDeletedMember2);
//        System.out.println(findDeletedMember2.getName());
//    }
//
//    @Test
//    void findByNameAndPhoneNumberTest() {
//        List<MemberEntity> members = memberRepository.findByNameAndPhoneNumber("유저2", "01084819926");
//        members.forEach(memberEntity -> System.out.println(memberEntity.getName()));
//    }
//
//}