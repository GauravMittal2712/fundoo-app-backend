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
public class NoteRequestDto {

    @NotBlank(message = ValidationConstants.FIELD_REQUIRED)
    @Size(max = ValidationConstants.NOTE_TITLE_MAX, message = "Title must be less than " + ValidationConstants.NOTE_TITLE_MAX + " characters")
    private String title;

    @Size(max = ValidationConstants.NOTE_DESC_MAX, message = "Description must be less than " + ValidationConstants.NOTE_DESC_MAX + " characters")
    private String description;

    @Size(max = ValidationConstants.COLOR_MAX, message = "Color must be less than " + ValidationConstants.COLOR_MAX + " characters")
    private String color;

    private boolean pinned;
    private boolean archived;
    private boolean trashed;
}
