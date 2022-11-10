package com.example.hsap.repository;

import com.example.hsap.model.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface MemberRepository extends JpaRepository<MemberEntity, String> {
    public boolean existsByEmail(String email);
    public MemberEntity findByEmail(String email);
    public MemberEntity findByEmailAndNameAndPhoneNumber(String email, String name, String phoneNumber);
    public MemberEntity findByEmailAndPassword(String email, String password);

    public List<MemberEntity> findByNameAndPhoneNumber(String name, String phoneNumber);

    // 부서별로 삭제된 항목들 가져오기 (복원에 사용)
    @Query(value = "select * from member where department_id = :department_id and deleted = true", nativeQuery = true)
    List<MemberEntity> findDeletedMembersByDepartment(@Param("department_id") String department_id);

    @Query(value = "select * from member where id = :id", nativeQuery = true)
    MemberEntity findByIdCustom(@Param("id") String id);

}
