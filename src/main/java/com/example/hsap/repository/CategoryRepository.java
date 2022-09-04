package com.example.hsap.repository;

import com.example.hsap.model.CategoryEntity;
import com.example.hsap.model.DepartmentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<CategoryEntity, String> {
    List<CategoryEntity> findByDepartment(String department);
    boolean existsByTitleAndDepartment(String title, DepartmentEntity department);
}
