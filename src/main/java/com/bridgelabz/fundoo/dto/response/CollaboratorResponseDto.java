package com.bridgelabz.fundoo.dto.response;

import com.bridgelabz.fundoo.entity.enums.CollaboratorRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CollaboratorResponseDto {
    private Long id;
    private Long noteId;
    private Long userId;
    private String userEmail;
    private String userFirstName;
    private String userLastName;
    private CollaboratorRole role;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
