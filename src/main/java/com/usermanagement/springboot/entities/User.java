package com.usermanagement.springboot.entities;


import lombok.*;
import org.hibernate.annotations.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.*;
import java.util.Date;
import java.util.UUID;


@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
@EntityListeners(AuditingEntityListener.class)
@SQLDelete(sql = "UPDATE users SET deleted = true ,deleted_at= CURRENT_TIMESTAMP WHERE id=?")
@Where(clause = "deleted=false")
public class User {

    /*
      Automatically generate the primary key and
      (for Auto generation type persistence provider should
      automatically pick an appropriate strategy for the particular database.)
      IDENTITY. Indicates that the persistence provider must assign primary keys
      for the entity using a database identity column
    */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private UUID uuid;

    @Column(name = "user_name", length = 50, unique = true)
    private String userName;
    @Column( length = 255)
    private String password;
    @Column(name = "first_name", length = 50)
    private String firstName;
    @Column(name = "last_name",length = 50)
    private String lastName;
    @Column(nullable = false)
    private String role;
    @Column(name = "created_at")
    @CreatedDate
    private Date createdAt;
    @Column(name="updated_at")
    @LastModifiedDate
    private Date updatedAt;
    @Column
    private boolean deleted = Boolean.FALSE;
    @Column(name="deleted_at")
    private Date deletedAt;


    @Override
    public String toString() {
        return "User{" +
                "uuid=" + uuid +
                ", userName='" + userName + '\'' +
                ", password='" + password + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", role='" + role + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", deleted=" + deleted +
                ", deletedAt=" + deletedAt +
                '}';
    }
}
