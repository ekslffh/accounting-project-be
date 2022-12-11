package com.example.hsap.repository;

import com.example.hsap.model.PeopleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PeopleRepository extends JpaRepository<PeopleEntity, String> {
}
