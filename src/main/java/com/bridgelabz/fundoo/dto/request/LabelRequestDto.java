package com.bridgelabz.fundoo.dto.request;

import jakarta.validation.constraints.NotBlank;
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
public class LabelRequestDto {

    @NotBlank(message = ValidationConstants.FIELD_REQUIRED)
    @Size(max = ValidationConstants.LABEL_NAME_MAX, message = "Label name must be less than " + ValidationConstants.LABEL_NAME_MAX + " characters")
    private String name;
}
