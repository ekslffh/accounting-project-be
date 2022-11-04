package com.example.hsap.repository;

import com.example.hsap.model.DepartmentEntity;
import com.example.hsap.model.HistoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface HistoryRepository extends JpaRepository<HistoryEntity, String> {
    List<HistoryEntity> findByDepartmentAndUseDateBetween(DepartmentEntity department, LocalDateTime start, LocalDateTime end);
}
