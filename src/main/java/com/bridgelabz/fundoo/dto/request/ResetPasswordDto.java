package com.bridgelabz.fundoo.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.bridgelabz.fundoo.constant.ValidationConstants;
import jakarta.validation.constraints.Pattern;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResetPasswordDto {

    @NotBlank(message = ValidationConstants.FIELD_REQUIRED)
    private String token;

    @NotBlank(message = ValidationConstants.FIELD_REQUIRED)
    @Size(min = ValidationConstants.PASSWORD_MIN, max = ValidationConstants.PASSWORD_MAX, message = ValidationConstants.PASSWORD_INVALID)
    @Pattern(regexp = ValidationConstants.PASSWORD_REGEX, message = ValidationConstants.PASSWORD_INVALID)
    private String newPassword;
}
