package com.bridgelabz.fundoo.mapper;

import com.bridgelabz.fundoo.dto.request.*;
import com.bridgelabz.fundoo.dto.response.*;
import com.bridgelabz.fundoo.entity.*;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class EntityMapper {

    public UserResponseDto toUserResponseDto(User user) {
        if (user == null) return null;
        return UserResponseDto.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .role(user.getRole())
                .active(user.isActive())
                .verified(user.isVerified())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }

    public NoteResponseDto toNoteResponseDto(Note note) {
        if (note == null) return null;

        Set<LabelResponseDto> labels = Collections.emptySet();
        if (note.getLabels() != null) {
            labels = note.getLabels().stream()
                    .map(this::toLabelResponseDto)
                    .collect(Collectors.toSet());
        }

        java.util.List<ReminderResponseDto> reminders = Collections.emptyList();
        if (note.getReminders() != null) {
            reminders = note.getReminders().stream()
                    .map(this::toReminderResponseDto)
                    .collect(Collectors.toList());
        }

        java.util.List<CollaboratorResponseDto> collaborators = Collections.emptyList();
        if (note.getCollaborators() != null) {
            collaborators = note.getCollaborators().stream()
                    .map(this::toCollaboratorResponseDto)
                    .collect(Collectors.toList());
        }

        return NoteResponseDto.builder()
                .id(note.getId())
                .title(note.getTitle())
                .description(note.getDescription())
                .color(note.getColor())
                .pinned(note.isPinned())
                .archived(note.isArchived())
                .trashed(note.isTrashed())
                .ownerId(note.getOwner() != null ? note.getOwner().getId() : null)
                .ownerEmail(note.getOwner() != null ? note.getOwner().getEmail() : null)
                .labels(labels)
                .reminders(reminders)
                .collaborators(collaborators)
                .createdAt(note.getCreatedAt())
                .updatedAt(note.getUpdatedAt())
                .build();
    }

    public LabelResponseDto toLabelResponseDto(Label label) {
        if (label == null) return null;
        return LabelResponseDto.builder()
                .id(label.getId())
                .name(label.getName())
                .userId(label.getUser() != null ? label.getUser().getId() : null)
                .createdAt(label.getCreatedAt())
                .updatedAt(label.getUpdatedAt())
                .build();
    }

    public CollaboratorResponseDto toCollaboratorResponseDto(Collaborator collaborator) {
        if (collaborator == null) return null;
        return CollaboratorResponseDto.builder()
                .id(collaborator.getId())
                .noteId(collaborator.getNote() != null ? collaborator.getNote().getId() : null)
                .userId(collaborator.getUser() != null ? collaborator.getUser().getId() : null)
                .userEmail(collaborator.getUser() != null ? collaborator.getUser().getEmail() : null)
                .userFirstName(collaborator.getUser() != null ? collaborator.getUser().getFirstName() : null)
                .userLastName(collaborator.getUser() != null ? collaborator.getUser().getLastName() : null)
                .role(collaborator.getRole())
                .createdAt(collaborator.getCreatedAt())
                .updatedAt(collaborator.getUpdatedAt())
                .build();
    }

    public ReminderResponseDto toReminderResponseDto(Reminder reminder) {
        if (reminder == null) return null;
        return ReminderResponseDto.builder()
                .id(reminder.getId())
                .noteId(reminder.getNote() != null ? reminder.getNote().getId() : null)
                .remindAt(reminder.getRemindAt())
                .status(reminder.getStatus())
                .notified(reminder.isNotified())
                .createdAt(reminder.getCreatedAt())
                .updatedAt(reminder.getUpdatedAt())
                .build();
    }
}
