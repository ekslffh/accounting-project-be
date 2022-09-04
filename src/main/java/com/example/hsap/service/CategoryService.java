package com.example.hsap.service;

import com.example.hsap.model.CategoryEntity;
import com.example.hsap.model.DepartmentEntity;
import com.example.hsap.model.HistoryEntity;
import com.example.hsap.repository.CategoryRepository;
import com.example.hsap.repository.DepartmentRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private DepartmentRepository departmentRepository;

    public List<CategoryEntity> create(CategoryEntity categoryEntity) {
        validate(categoryEntity);
        // 해당 부서 존재 여부 검사
        if (!departmentRepository.existsByName(categoryEntity.getDepartment().getName())) {
            log.warn("This department is not exists");
            throw new RuntimeException("This department is not exits");
        }
        DepartmentEntity department = departmentRepository.findByName(categoryEntity.getDepartment().getName());
        // 중복 검사 : 같은 부서 내에 같은 이름의 카테고리가 존재하면 안된다.
        if (categoryRepository.existsByTitleAndDepartment(categoryEntity.getTitle(), department)) {
            log.warn("The same category exists within that department");
            throw new RuntimeException("The same category exists within that department");
        }
        categoryEntity.setDepartment(department);
        categoryRepository.save(categoryEntity);
        return categoryEntity.getDepartment().getCategories();
    }
    public List<CategoryEntity> retrieveByDepartment(String departmentName) {
        if (departmentName == null) throw new RuntimeException("department is null");
        DepartmentEntity department = departmentRepository.findByName(departmentName);
        return department.getCategories();
    }

    public List<CategoryEntity> update(CategoryEntity categoryEntity) {
        validate(categoryEntity);
        if (categoryEntity.getId() == null) {
            log.warn("This category's id is null");
            throw new RuntimeException("This category's id is null");
        }
        Optional<CategoryEntity> optionalEntity =  categoryRepository.findById(categoryEntity.getId());
        if (optionalEntity.isPresent()) {
            CategoryEntity original = optionalEntity.get();
            original.setTitle(categoryEntity.getTitle());
            original.setDescription(categoryEntity.getDescription());
            categoryRepository.save(original);
            retrieveByDepartment(categoryEntity.getDepartment().getName());
            return original.getDepartment().getCategories();
        }
        else {
            log.warn("This category is not exits");
            throw new RuntimeException("This category is not exits");
        }
    }
    public List<CategoryEntity> delete(CategoryEntity categoryEntity) {
        validate(categoryEntity);
        try {
            categoryRepository.delete(categoryEntity);
            return retrieveByDepartment(categoryEntity.getDepartment().getName());
        } catch (Exception ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }

    public List<HistoryEntity> getHistories(CategoryEntity categoryEntity) {
        validate(categoryEntity);
        Optional<CategoryEntity> optionalEntity = categoryRepository.findById(categoryEntity.getId());
        if (optionalEntity.isPresent()) {
            CategoryEntity findEntity = optionalEntity.get();
            return findEntity.getHistories();
        }
        else {
            log.warn("This category is not exits");
            throw new RuntimeException("This category is not exists");
        }
    }

    public void validate(CategoryEntity entity) {
        if (entity == null) {
            log.warn("This category is null");
            throw new RuntimeException("This category is null");
        }
    }

}
