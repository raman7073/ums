package com.usermanagement.springboot.entities;


import com.usermanagement.springboot.dtos.UserDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.Where;
import org.springframework.core.convert.converter.Converter;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.UUID;


@Data
@Entity
@Table(name = "users")
@SQLDelete(sql = "UPDATE users SET deleted = true ,deleted_at= CURRENT_TIMESTAMP WHERE id=?")
@Where(clause = "deleted=false")
@NoArgsConstructor
@AllArgsConstructor
public class User implements Converter<User, UserDTO> {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private UUID userId;

    @Column(length = 50, unique = true)
    private String username;

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

        return UserDTO.builder().username(user.getUsername()).
                userId(user.getUserId()).
                firstName(user.getFirstName()).
                lastName(user.getLastName()).
                role(user.getRole()).
                createdAt(user.getCreatedAt()).
                updatedAt(user.getUpdatedAt()).build();
    }


}
