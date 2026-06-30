package com.bridgelabz.fundoo.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

import com.bridgelabz.fundoo.constant.ValidationConstants;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReminderRequestDto {

    @NotNull(message = ValidationConstants.FIELD_REQUIRED)
    private LocalDateTime remindAt;
}
