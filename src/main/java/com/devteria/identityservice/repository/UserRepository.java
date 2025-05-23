package com.devteria.identityservice.repository;

import java.util.Optional;

import com.devteria.identityservice.entity.Hotel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.devteria.identityservice.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    boolean existsByUsername(String username);
    Optional<User> findByUsername(String username);
    @Query(value="SELECT COUNT(*) FROM user", nativeQuery = true)
    long countTotalUser();
}
