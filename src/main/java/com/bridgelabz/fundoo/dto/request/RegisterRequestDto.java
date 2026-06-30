package com.bridgelabz.fundoo.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.bridgelabz.fundoo.constant.ValidationConstants;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequestDto {

    @NotBlank(message = ValidationConstants.FIELD_REQUIRED)
    @Size(min = ValidationConstants.NAME_MIN, max = ValidationConstants.NAME_MAX, message = ValidationConstants.NAME_INVALID)
    @Pattern(regexp = ValidationConstants.NAME_REGEX, message = ValidationConstants.NAME_INVALID)
    private String firstName;

    @Size(min = ValidationConstants.NAME_MIN, max = ValidationConstants.NAME_MAX, message = ValidationConstants.NAME_INVALID)
    @Pattern(regexp = ValidationConstants.NAME_REGEX, message = ValidationConstants.NAME_INVALID)
    private String lastName;

    @NotBlank(message = ValidationConstants.FIELD_REQUIRED)
    @Pattern(regexp = ValidationConstants.EMAIL_REGEX, message = ValidationConstants.EMAIL_INVALID)
    private String email;

    @NotBlank(message = ValidationConstants.FIELD_REQUIRED)
    @Size(min = ValidationConstants.PASSWORD_MIN, max = ValidationConstants.PASSWORD_MAX, message = ValidationConstants.PASSWORD_INVALID)
    @Pattern(regexp = ValidationConstants.PASSWORD_REGEX, message = ValidationConstants.PASSWORD_INVALID)
    private String password;

    @Pattern(regexp = "^$|" + ValidationConstants.PHONE_REGEX, message = ValidationConstants.PHONE_INVALID)
    private String phoneNumber;
}
