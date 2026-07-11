package com.bridgelabz.fundoo.dto.request;

import com.bridgelabz.fundoo.entity.enums.CollaboratorRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
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
public class CollaboratorRequestDto {

    @NotBlank(message = ValidationConstants.FIELD_REQUIRED)
    @Pattern(regexp = ValidationConstants.EMAIL_REGEX, message = ValidationConstants.EMAIL_INVALID)
    private String email;

    private CollaboratorRole role;
}
