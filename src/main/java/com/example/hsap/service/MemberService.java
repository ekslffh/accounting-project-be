package com.example.hsap.service;

import com.example.hsap.model.CategoryEntity;
import com.example.hsap.model.DepartmentEntity;
import com.example.hsap.model.HistoryEntity;
import com.example.hsap.model.MemberEntity;
import com.example.hsap.repository.DepartmentRepository;
import com.example.hsap.repository.MemberRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class MemberService {
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    DepartmentRepository departmentRepository;

    public MemberEntity create(MemberEntity entity) {
        validate(entity);
        // email 중복 여부 확인
        final String email = entity.getEmail();
        if (memberRepository.existsByEmail(email)) {
            throw new RuntimeException("Email already exists : " + email);
        }
        // 등록 부서의 존재 유무 확인
        if (!departmentRepository.existsByName(entity.getDepartment().getName())) {
            log.warn("This department is not exists");
            throw new RuntimeException("This department is not exists");
        }
        DepartmentEntity departmentEntity = departmentRepository.findByName(entity.getDepartment().getName());
        entity.setDepartment(departmentEntity);
        entity.setCreatedAt(LocalDateTime.now());
        return memberRepository.save(entity);
    }

    public List<MemberEntity> retrieveAll() {
        return memberRepository.findAll();
    }

    public MemberEntity searchById(String id) {
        Optional<MemberEntity> optionalMemberEntity = memberRepository.findById(id);
        if (optionalMemberEntity.isEmpty()) {
            log.warn("This member's id is not exists");
            throw new RuntimeException("This member's id is not exists");
        }
        else return optionalMemberEntity.get();
    }

    public MemberEntity getByCredentials(final String email, final String password, final PasswordEncoder encoder) {
        final MemberEntity originalMember = memberRepository.findByEmail(email);

        if (originalMember != null &&
            encoder.matches(password, originalMember.getPassword())) {
            return originalMember;
        }
        return null;
    }

//    public MemberEntity update(MemberEntity entity) {
//        validate(entity);
//        MemberEntity findEntity = getByCredentials(entity.getEmail(), entity.getPassword());
//        if (findEntity == null) {
//            throw new RuntimeException("Member is not exists");
//        }
//        findEntity.setPassword(entity.getPassword());
//        findEntity.setName(entity.getName());
//        findEntity.setBirth(entity.getBirth());
//        findEntity.setGender(entity.getGender());
//        findEntity.setAsset(entity.getAsset());
//        return memberRepository.save(findEntity);
//    }

    public void delete(MemberEntity entity) {
        validate(entity);
        try {
            memberRepository.delete(entity);
        } catch (Exception ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }

    public List<HistoryEntity> getHistories(String memberId) {
        MemberEntity member = memberRepository.findById(memberId).orElseThrow(RuntimeException::new);
        return member.getHistories();
    }

    public List<CategoryEntity> getCategories(String memberId) {
        MemberEntity memberEntity = memberRepository.findById(memberId).orElseThrow(RuntimeException::new);
        return memberEntity.getDepartment().getCategories();
    }

    public void validate(MemberEntity entity) {
        if (entity == null || entity.getEmail() == null) {
            throw new RuntimeException("Invalid arguments");
        }
    }
}
