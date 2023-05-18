package com.usermanagement.springboot.daos;

import com.usermanagement.springboot.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.usermanagement.springboot.common.Constants.FINDALLQUERY;

public interface UserDAO extends JpaRepository<User, UUID> {

    @Override
    @Query(value = FINDALLQUERY, nativeQuery = true)
    List<User> findAll();

    Optional<User> findUserByUsername(String username);

    boolean existsByUsername(String userName);

}
