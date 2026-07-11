package com.bridgelabz.fundoo.dto.response;

import com.bridgelabz.fundoo.entity.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseDto {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private Role role;
    private boolean active;
    private boolean verified;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
