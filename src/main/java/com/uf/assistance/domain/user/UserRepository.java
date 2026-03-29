package com.uf.assistance.domain.user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface UserRepository extends JpaRepository<User, Long> {
    // 로그인ID
    Optional<User> findByUserId(String userId);
    Optional<User> findByEmail(String usermail); //Jpa NamedQuery 작동

    boolean existsByUserId(String userId);
}
