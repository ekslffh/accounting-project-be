package com.example.hsap.service;

import com.example.hsap.model.CategoryEntity;
import com.example.hsap.model.DepartmentEntity;
import com.example.hsap.model.HistoryEntity;
import com.example.hsap.model.MemberEntity;
import com.example.hsap.repository.DepartmentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class DepartmentService {
    private final DepartmentRepository departmentRepository;

    public List<DepartmentEntity> create(DepartmentEntity departmentEntity) {
            validate(departmentEntity);
            departmentRepository.save(departmentEntity);
            return retrieveAll();
    }
    public List<DepartmentEntity> retrieveAll() {
        List<DepartmentEntity> allDepartment = departmentRepository.findAll();
        // 관리부서 제외하고 조회하기
        allDepartment.remove(0);
        return allDepartment;
    }
    public DepartmentEntity retrieve(String name) {
        DepartmentEntity department = departmentRepository.findByName(name);
        if (department == null) {
            log.warn("해당 부서가 존재하지 않습니다.");
            throw new RuntimeException("해당 부서가 존재하지 않습니다.");
        }
        else return department;
    }

    // 수정 가능 내역: 이름
    public List<DepartmentEntity> update(DepartmentEntity departmentEntity) {
           validate(departmentEntity);
           if (departmentEntity.getId() == null) {
               log.warn("This department entity's id is null");
               throw new RuntimeException("This department entity's id is null");
           }
            Optional<DepartmentEntity> optionalDepartment = departmentRepository.findById(departmentEntity.getId());
            // 아이디를 가지고 찾은 부서가 존재한다면
            if (optionalDepartment.isPresent()) {
                DepartmentEntity originalDepartment = optionalDepartment.get();
                if (departmentEntity.getName() != null) originalDepartment.setName(departmentEntity.getName());
//                original.setAsset(departmentEntity.getAsset());
                departmentRepository.save(originalDepartment);
                return retrieveAll();
            } else {
                log.warn("해당 부서가 존재하지 않습니다.");
                throw new RuntimeException("해당 부서가 존재하지 않습니다.");
            }
    }
    public List<DepartmentEntity> delete(DepartmentEntity departmentEntity) {
        try {
            validate(departmentEntity);
            if (departmentEntity.getId() == null) {
                log.warn("This department entity's id is null");
                throw new RuntimeException("This department entity's id is null");
            }
            departmentRepository.delete(departmentEntity);
            return retrieveAll();
        } catch (Exception ex) {
            throw new RuntimeException("Delete department error: " + ex.getMessage());
        }
    }

    // 부서의 회원들 목록 조회하기
    public List<MemberEntity> getMembers(String name) {
        DepartmentEntity department = departmentRepository.findByName(name);
        return department.getMembers();
    }

    // 부서의 카테고리들 조회하기
    public List<CategoryEntity> getCategories(String name) {
        DepartmentEntity department = departmentRepository.findByName(name);
        return department.getCategories();
    }

    // 부서의 사용내역들 조회하기
    public List<HistoryEntity> getHistories(String name) {
        DepartmentEntity department = departmentRepository.findByName(name);
        return department.getHistories();
    }

    public void validate(DepartmentEntity departmentEntity) {
        if (departmentEntity == null) {
            log.warn("This department entity is null");
            throw new RuntimeException("This department entity is null");
        }
    }
    
}
