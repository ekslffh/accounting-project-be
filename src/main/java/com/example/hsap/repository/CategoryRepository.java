package com.example.hsap.repository;

import com.example.hsap.model.CategoryEntity;
import com.example.hsap.model.DepartmentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<CategoryEntity, String> {
    boolean existsByTitleAndDepartment(String title, DepartmentEntity department);

    // 부서별로 삭제된 항목들 가져오기 (복원에 사용)
    @Query(value = "select * from category where department_id = :department_id and deleted = true", nativeQuery = true)
    List<CategoryEntity> findDeletedCategoriesByDepartment(@Param("department_id") String department_id);

    @Query(value = "select * from category where id = :id", nativeQuery = true)
    Optional<CategoryEntity> findByIdCustom(@Param("id") String id);
}
