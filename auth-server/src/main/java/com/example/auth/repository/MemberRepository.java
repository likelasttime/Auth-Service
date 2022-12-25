package com.example.auth.repository;

import com.example.auth.model.Member;
import com.example.auth.model.UserRole;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface MemberRepository extends CrudRepository<Member, Long> {

    Member findByUsername(String username);

    List<Member> findAll();

    Page<Member> findAllByRoleNotLike(UserRole role, Pageable pageable);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    void deleteByUsername(String username);
}
