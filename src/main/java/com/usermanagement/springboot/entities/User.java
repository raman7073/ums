package com.usermanagement.springboot.entities;


import com.usermanagement.springboot.dtos.UserDTO;
import lombok.Builder;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.Where;
import org.springframework.core.convert.converter.Converter;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.UUID;


@Builder
@Data
@Entity
@Table(name = "users")
@SQLDelete(sql = "UPDATE users SET deleted = true ,deleted_at= CURRENT_TIMESTAMP WHERE id=?")
@Where(clause = "deleted=false")
public class User implements Converter<User, UserDTO> {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private UUID userId;

    @Column(name = "user_name", length = 50, unique = true)
    private String userName;

    @Column
    private String password;

    @Column(name = "first_name", length = 50)
    private String firstName;

    @Column(name = "last_name", length = 50)
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
    private Timestamp deletedAt;

    @Override
    public UserDTO convert(User user) {

        return UserDTO.builder().userName(user.getUserName()).
                userId(user.getUserId()).
                firstName(user.getFirstName()).
                lastName(user.getLastName()).
                role(user.getRole()).
                createdAt(user.getCreatedAt()).
                updatedAt(user.getUpdatedAt()).build();
    }
}
