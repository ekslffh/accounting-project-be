package com.example.hsap.service;

import com.example.hsap.model.CategoryEntity;
import com.example.hsap.model.DepartmentEntity;
import com.example.hsap.model.HistoryEntity;
import com.example.hsap.model.MemberEntity;
import com.example.hsap.repository.DepartmentRepository;
import com.example.hsap.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final DepartmentRepository departmentRepository;

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
        return memberRepository.save(entity);
    }

    public boolean checkDuplicateEmail(String email) {
        // 중복되면 true return
        return memberRepository.existsByEmail(email);
    }

    public List<MemberEntity> retrieveAll() {
        return memberRepository.findAll();
    }

    public List<MemberEntity> retrieveDeletedByDepartment(String departmentName) {
        if (departmentName == null) throw new RuntimeException("department is null");
        DepartmentEntity department = departmentRepository.findByName(departmentName);
        return memberRepository.findDeletedMembersByDepartment(department.getId());
    }

    public List<MemberEntity> recover(String id) {
            MemberEntity member = memberRepository.findByIdCustom(id);
            member.setDeleted(false);
            memberRepository.save(member);

        return retrieveDeletedByDepartment(member.getDepartment().getName());
    }

    public MemberEntity searchById(String id) {
        Optional<MemberEntity> optionalMemberEntity = memberRepository.findById(id);
        if (optionalMemberEntity.isEmpty()) {
            log.warn("This member's id is not exists");
            throw new RuntimeException("This member's id is not exists");
        }
        else return optionalMemberEntity.get();
    }

    public MemberEntity searchByEmail(String email) {
        MemberEntity member = memberRepository.findByEmail(email);
        if (member == null) throw new RuntimeException("해당 계정이 존재하지 않습니다.");
        else return member;
    }

    public MemberEntity searchByEmailAndName(String email, String name, String phoneNumber) {
        MemberEntity member = memberRepository.findByEmailAndNameAndPhoneNumber(email, name, phoneNumber);
        if (member == null) throw new RuntimeException("해당 계정이 존재하지 않습니다.");
        else return member;
    }

    public List<String> getEmailList(MemberEntity member) {
        if (member == null || member.getName() == null || member.getPhoneNumber() == null) {
            throw new RuntimeException("멤버 객체의 형식이 잘못되었습니다.");
        }
        List<MemberEntity> memberEntities = memberRepository.findByNameAndPhoneNumber(member.getName(), member.getPhoneNumber());
        if (memberEntities == null) throw new RuntimeException("해당하는 계정이 존재하지 않습니다.");
        List<String> emailList = memberEntities.stream().map(memberEntity -> memberEntity.getEmail()).toList();
        return emailList;
    }

    public MemberEntity getByCredentials(final String email, final String password, final PasswordEncoder encoder) {
        final MemberEntity originalMember = memberRepository.findByEmail(email);

        if (originalMember != null &&
            encoder.matches(password, originalMember.getPassword())) {
            return originalMember;
        }
        return null;
    }

    public MemberEntity update(MemberEntity entity) {
        MemberEntity findEntity = searchById(entity.getId());
        if (findEntity == null) {
            throw new RuntimeException("Member is not exists");
        }
        findEntity.setBirth(entity.getBirth());
        findEntity.setPhoneNumber(entity.getPhoneNumber());
        findEntity.setName(entity.getName());
        return memberRepository.save(findEntity);
    }

    public MemberEntity updatePassword(MemberEntity entity) {
        return memberRepository.save(entity);
    }

    public void delete(MemberEntity entity) {
        MemberEntity foundMember = memberRepository.findByEmail(entity.getEmail());
        String authorities = foundMember.getGrantedAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));
        if (authorities.contains("ROLE_LEADER")) {
            throw new RuntimeException("부서의 리더는 삭제가 불가능 합니다.");
        }
        foundMember.setDeleted(true);
        memberRepository.save(foundMember);
    }

    public List<HistoryEntity> getHistories(String memberId, String year) {
        MemberEntity member = memberRepository.findById(memberId).orElseThrow(RuntimeException::new);
        List<HistoryEntity> historyEntities = member.getHistories();
        return historyEntities.stream().filter(historyEntity -> historyEntity.getUseDate().getYear() == Integer.parseInt(year)).toList();
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
