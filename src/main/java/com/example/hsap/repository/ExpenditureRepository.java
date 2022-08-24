package com.example.hsap.repository;

import com.example.hsap.model.ExpenditureEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExpenditureRepository extends JpaRepository<ExpenditureEntity, String> {
    List<ExpenditureEntity> findByUserId(String userId);
}
