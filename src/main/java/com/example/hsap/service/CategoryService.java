package com.example.hsap.service;

import com.example.hsap.model.CategoryEntity;
import com.example.hsap.model.DepartmentEntity;
import com.example.hsap.model.HistoryEntity;
import com.example.hsap.repository.CategoryRepository;
import com.example.hsap.repository.DepartmentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final DepartmentRepository departmentRepository;

    public List<CategoryEntity> create(CategoryEntity categoryEntity) {
        validate(categoryEntity);
        // 해당 부서 존재 여부 검사
        if (!departmentRepository.existsByName(categoryEntity.getDepartment().getName())) {
            log.warn("해당 부서가 존재하지 않습니다.");
            throw new RuntimeException("해당 부서가 존재하지 않습니다.");
        }
        DepartmentEntity department = departmentRepository.findByName(categoryEntity.getDepartment().getName());
        // 중복 검사 : 같은 부서 내에 같은 이름의 카테고리가 존재하면 안된다.
        if (categoryRepository.existsByTitleAndDepartment(categoryEntity.getTitle(), department)) {
            log.warn("한 부서내에서 카테고리명은 중복될 수 없습니다.");
            throw new RuntimeException("한 부서내에서 카테고리명은 중복될 수 없습니다.");
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
        Optional<CategoryEntity> optionalEntity = categoryRepository.findById(categoryEntity.getId());
        boolean isTitleChanged = false;
        if (optionalEntity.isPresent()) {
            CategoryEntity original = optionalEntity.get();
            if (categoryEntity.getTitle() != null && !categoryEntity.getTitle().equals(original.getTitle())) {
                original.setTitle(categoryEntity.getTitle());
                isTitleChanged = true;
            }
            if (categoryEntity.getDescription() != null) original.setDescription(categoryEntity.getDescription());
//            DepartmentEntity department = departmentRepository.findByName(categoryEntity.getDepartment().getName());
            // 중복 검사 : 같은 부서 내에 같은 이름의 카테고리가 존재하면 안된다.
            // 예외 : 만약 타이틀이 바뀌지 않았다면 넘어가기!!
            if (isTitleChanged && categoryRepository.existsByTitleAndDepartment(categoryEntity.getTitle(), original.getDepartment())) {
                log.warn("한 부서내에서 카테고리명은 중복될 수 없습니다.");
                throw new RuntimeException("한 부서내에서 카테고리명은 중복될 수 없습니다.");
            }
            categoryRepository.save(original);
//            retrieveByDepartment(categoryEntity.getDepartment().getName());
//            DepartmentEntity department = departmentRepository.findByName(original.getDepartment().getName());
//            return department.getCategories();
            return original.getDepartment().getCategories();
        }
        else {
            log.warn("This category is not exits");
            throw new RuntimeException("This category is not exits");
        }
    }
    public List<CategoryEntity> delete(CategoryEntity categoryEntity) {
        validate(categoryEntity);
//        try {
//            categoryRepository.delete(categoryEntity);
//            return retrieveByDepartment(categoryEntity.getDepartment().getName());
//        } catch (Exception ex) {
//            throw new RuntimeException(ex.getMessage());
//        }
        CategoryEntity foundCategory = categoryRepository.findById(categoryEntity.getId()).orElseThrow(RuntimeException::new);
        foundCategory.setDeleted(true);
        categoryRepository.save(foundCategory);
        return retrieveByDepartment(foundCategory.getDepartment().getName());
    }

    public List<HistoryEntity> getHistories(String id) {
        Optional<CategoryEntity> optionalEntity = categoryRepository.findById(id);
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
            log.warn("This categoryEntity is null");
            throw new RuntimeException("This categoryEntity is null");
        }
    }

}
