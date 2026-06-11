package com.bridgelabz.fundoo.entity;

import com.bridgelabz.fundoo.entity.enums.Role;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Entity
@Table (
        name = "users",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_users_email",
                        columnNames = "email"
                )
        },
        indexes = {
                @Index(name = "idx_users_email", columnList = "email")
        }
)


@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false,length = 100)
    private String firstName;

    @Column(length = 100)
    private String lastName;

    @Column(nullable = false,length = 255)
    private String email;

    @Column(name = "phone_number",length = 20)
    private String phoneNumber;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    private boolean active = true;

    private boolean verified = false;

    private boolean deleted = false;

    private LocalDateTime lastLoginAt;

    private LocalDateTime deletedAt;

    @OneToMany(mappedBy = "owner", fetch = FetchType.LAZY)
    private List<Note> notes = new ArrayList<>();

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<Label> labels = new ArrayList<>();

}
