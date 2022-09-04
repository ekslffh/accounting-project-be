package com.example.hsap.service;

import com.example.hsap.model.CategoryEntity;
import com.example.hsap.model.DepartmentEntity;
import com.example.hsap.model.HistoryEntity;
import com.example.hsap.model.MemberEntity;
import com.example.hsap.repository.DepartmentRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class DepartmentService {
    @Autowired
    DepartmentRepository departmentRepository;

    public List<DepartmentEntity> create(DepartmentEntity departmentEntity) {
            validate(departmentEntity);
            departmentRepository.save(departmentEntity);
            return retrieveAll();
    }
    public List<DepartmentEntity> retrieveAll() {
        return departmentRepository.findAll();
    }

    public DepartmentEntity retrieve(String name) {
        return departmentRepository.findByName(name);
    }
    public List<DepartmentEntity> update(DepartmentEntity departmentEntity) {
           validate(departmentEntity);
           if (departmentEntity.getId() == null) {
               log.warn("This department entity's id is null");
               throw new RuntimeException("This department entity's id is null");
           }
            Optional<DepartmentEntity> optionalEntity = departmentRepository.findById(departmentEntity.getId());
            // 아이디를 가지고 찾은 부서가 존재한다면
            if (optionalEntity.isPresent()) {
                DepartmentEntity original = optionalEntity.get();
                original.setName(departmentEntity.getName());
                original.setAsset(departmentEntity.getAsset());
                departmentRepository.save(original);
                return retrieveAll();
            } else {
                log.warn("This department is not exist");
                throw new RuntimeException("This department is not exist");
            }
    }
    public List<DepartmentEntity> delete(DepartmentEntity entity) {
        try {
            validate(entity);
            departmentRepository.delete(entity);
            return retrieveAll();
        } catch (Exception ex) {
            throw new RuntimeException("Delete department error: " + ex.getMessage());
        }
    }

    // 부서의 회원들 목록 조회하기
    public List<MemberEntity> getMembers(DepartmentEntity departmentEntity) {
        validate(departmentEntity);
        DepartmentEntity department = departmentRepository.findByName(departmentEntity.getName());
        return department.getMembers();
    }

    // 부서의 카테고리들 조회하기
    public List<CategoryEntity> getCategories(DepartmentEntity departmentEntity) {
        validate(departmentEntity);
        DepartmentEntity department = departmentRepository.findByName(departmentEntity.getName());
        return department.getCategories();
    }

    // 부서의 사용내역들 조회하기
    public List<HistoryEntity> getHistories(DepartmentEntity departmentEntity) {
        validate(departmentEntity);
        DepartmentEntity department = departmentRepository.findByName(departmentEntity.getName());
        return department.getHistories();
    }

    public void validate(DepartmentEntity entity) {
        if (entity == null) {
            log.warn("This department entity is null");
            throw new RuntimeException("This department entity is null");
        }
    }
}
