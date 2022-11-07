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
            log.warn("카테고리가 중복되었습니다..");
            throw new RuntimeException("카테고리가 중복되었습니다.");
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

    public List<CategoryEntity> retrieveDeletedByDepartment(String departmentName) {
        if (departmentName == null) throw new RuntimeException("department is null");
        DepartmentEntity department = departmentRepository.findByName(departmentName);
        return categoryRepository.findDeletedCategoriesByDepartment(department.getId());
    }

    public List<CategoryEntity> recover(String id) {
        Optional<CategoryEntity> optionalCategory = categoryRepository.findByIdCustom(id);
        if (optionalCategory.isPresent()) {
            CategoryEntity category = optionalCategory.get();
            category.setDeleted(false);
            categoryRepository.save(category);
        }
        else throw new RuntimeException("해당 카테고리가 존재하지 않습니다.");

        return retrieveByDepartment(optionalCategory.get().getDepartment().getName());
    }

    public List<CategoryEntity> update(CategoryEntity categoryEntity) {
        validate(categoryEntity);
        if (categoryEntity.getId() == null) {
            log.warn("This category's id is null");
            throw new RuntimeException("This category's id is null");
        }
        Optional<CategoryEntity> optionalEntity = categoryRepository.findById(categoryEntity.getId());
        // 제목이 변경되었는지 확인하기
        boolean isTitleChanged = false;

        if (optionalEntity.isPresent()) {
            CategoryEntity original = optionalEntity.get();
            // 업데이트하려는 타이틀이 존재하고 기존의 타이틀과 다르다면 업데이트 하기
            if (categoryEntity.getTitle() != null && !categoryEntity.getTitle().equals(original.getTitle())) {
                original.setTitle(categoryEntity.getTitle());
                isTitleChanged = true;
            }
            if (categoryEntity.getDescription() != null) original.setDescription(categoryEntity.getDescription());

            // 중복 검사 : 같은 부서 내에 같은 이름의 카테고리가 존재하면 안된다.
            // 예외 : 만약 타이틀이 바뀌지 않았다면 넘어가기 (타이틀이 바뀌지 않은 상태에서 이 로직을 수행 시에 기존의 타이틀과의 충돌이 발생한다.)
            if (isTitleChanged && categoryRepository.existsByTitleAndDepartment(categoryEntity.getTitle(), original.getDepartment())) {
                log.warn("카테고리가 중복되었습니다.");
                throw new RuntimeException("카테고리가 중복되었습니다.");
            }
            categoryRepository.save(original);
            return original.getDepartment().getCategories();
        }
        else {
            log.warn("해당 카테고리가 존재하지 않습니다.");
            throw new RuntimeException("해당 카테고리가 존재하지 않습니다.");
        }
    }
    public List<CategoryEntity> delete(CategoryEntity categoryEntity) {
        validate(categoryEntity);
        CategoryEntity foundCategory = categoryRepository.findById(categoryEntity.getId()).orElseThrow(RuntimeException::new);
        // 실제로 삭제하지 않고 deleted 옵션 false 로 변경한다. (추후에 복원할 수 있다.)
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
