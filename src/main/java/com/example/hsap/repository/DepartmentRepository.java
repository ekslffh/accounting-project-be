package com.example.hsap.repository;

import com.example.hsap.model.DepartmentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DepartmentRepository extends JpaRepository<DepartmentEntity, String> {
    DepartmentEntity findByName(String name);
    boolean existsByName(String name);
}