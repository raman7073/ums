package com.usermanagement.springboot.daos;

import com.usermanagement.springboot.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserDAO extends JpaRepository<User, UUID> {
    @Override
    @Query(value = "SELECT * FROM users u WHERE u.deleted = false", nativeQuery = true)
    List<User> findAll();

    Optional<User> findUserByUsername(String username);

    boolean existsByUsername(String userName);

}
