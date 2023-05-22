package com.usermanagement.springboot.entities;


import com.usermanagement.springboot.dtos.UserDTO;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.Where;
import org.springframework.core.convert.converter.Converter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

import static com.usermanagement.springboot.common.Constants.*;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name = USERS)
@SQLDelete(sql = SQL_ON_DELETE_QUERY)
@Where(clause = DELETE_FALSE)
public class User implements Converter<UserDTO, User> {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = ID)
    private UUID userId;

    @Column(length = 50, unique = true)
    private String username;

    @Column
    private String password;

    @Column(name = FIRST_NAME, length = 50)
    private String firstName;

    @Column(name = LAST_NAME, length = 50)
    private String lastName;

    @Column(nullable = false)
    private String role;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @Column
    private boolean deleted = Boolean.FALSE;

    @Column
    private LocalDateTime deletedAt;

    @Override
    public User convert(UserDTO userDTO) {

        this.setUserId(userDTO.getUserId());
        this.setUsername(userDTO.getUsername());
        this.setPassword(userDTO.getPassword());
        this.setFirstName(userDTO.getFirstName());
        this.setLastName(userDTO.getLastName());
        this.setRole(userDTO.getRole());
        this.setCreatedAt(userDTO.getCreatedAt());
        this.setUpdatedAt(userDTO.getUpdatedAt());
        this.setDeletedAt(userDTO.getDeletedAt());
        return this;
    }
}
