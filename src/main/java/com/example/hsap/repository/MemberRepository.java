package com.example.hsap.repository;

import com.example.hsap.model.MemberEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<MemberEntity, String> {
    public boolean existsByEmail(String email);
    public MemberEntity findByEmail(String email);
    public MemberEntity findByEmailAndPassword(String email, String password);

    @EntityGraph(attributePaths = "authorities")
    public Optional<MemberEntity> findOneWithAuthoritiesByName(String name);
}
