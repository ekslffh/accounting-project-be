package com.example.hsap.repository;

import com.example.hsap.model.CategoryEntity;
import com.example.hsap.model.DepartmentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<CategoryEntity, String> {
    boolean existsByTitleAndDepartment(String title, DepartmentEntity department);

    // 부서별로 삭제된 항목들 가져오기 (복원에 사용)
    @Query(value = "select * from category where department_id = department_id and deleted = true", nativeQuery = true)
    List<CategoryEntity> findDeletedCategoriesByDepartment(String department_id);
}
