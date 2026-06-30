package com.bridgelabz.fundoo.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NoteResponseDto {
    private Long id;
    private String title;
    private String description;
    private String color;
    private boolean pinned;
    private boolean archived;
    private boolean trashed;
    private Long ownerId;
    private String ownerEmail;
    private Set<LabelResponseDto> labels;
    private List<ReminderResponseDto> reminders;
    private List<CollaboratorResponseDto> collaborators;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
