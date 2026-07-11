package com.bridgelabz.fundoo.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LabelResponseDto {
    private Long id;
    private String name;
    private Long userId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
