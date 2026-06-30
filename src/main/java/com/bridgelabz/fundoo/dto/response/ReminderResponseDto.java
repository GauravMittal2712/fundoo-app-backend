package com.bridgelabz.fundoo.dto.response;

import com.bridgelabz.fundoo.entity.enums.ReminderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReminderResponseDto {
    private Long id;
    private Long noteId;
    private LocalDateTime remindAt;
    private ReminderStatus status;
    private boolean notified;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
