package com.bridgelabz.fundoo.service.impl;

import com.bridgelabz.fundoo.constant.ErrorConstants;
import com.bridgelabz.fundoo.dto.request.*;
import com.bridgelabz.fundoo.dto.response.*;
import com.bridgelabz.fundoo.entity.Note;
import com.bridgelabz.fundoo.entity.Reminder;
import com.bridgelabz.fundoo.entity.User;
import com.bridgelabz.fundoo.entity.enums.ReminderStatus;
import com.bridgelabz.fundoo.exception.ResourceNotFoundException;
import com.bridgelabz.fundoo.mapper.EntityMapper;
import com.bridgelabz.fundoo.repository.ReminderRepository;
import com.bridgelabz.fundoo.service.NoteService;
import com.bridgelabz.fundoo.service.ReminderService;
import com.bridgelabz.fundoo.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ReminderServiceImpl implements ReminderService {

    private final ReminderRepository reminderRepository;
    private final NoteService noteService;
    private final UserService userService;
    private final EntityMapper entityMapper;

    @Override
    public ReminderResponseDto addReminderToNote(Long noteId, ReminderRequestDto dto) throws ResourceNotFoundException {
        User currentUser = userService.getAuthenticatedUser();
        log.info("Adding reminder to note ID {} by user {}", noteId, currentUser.getEmail());

        Note note = noteService.getNoteEntityById(noteId);
        verifyOwnershipOrCollaborator(note, currentUser);

        if (dto.getRemindAt().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Reminder time must be in the future");
        }

        Reminder reminder = Reminder.builder()
                .note(note)
                .remindAt(dto.getRemindAt())
                .status(ReminderStatus.PENDING)
                .notified(false)
                .build();

        Reminder savedReminder = reminderRepository.save(reminder);
        log.info("Reminder added successfully with ID {}", savedReminder.getId());
        return entityMapper.toReminderResponseDto(savedReminder);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReminderResponseDto> getRemindersForNote(Long noteId) throws ResourceNotFoundException {
        log.info("Fetching reminders for note ID {}", noteId);
        Note note = noteService.getNoteEntityById(noteId);
        return reminderRepository.findAllByNote(note).stream()
                .map(entityMapper::toReminderResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public ReminderResponseDto getReminderById(Long id) throws ResourceNotFoundException {
        log.info("Fetching reminder by ID {}", id);
        Reminder reminder = getReminderEntityById(id);
        return entityMapper.toReminderResponseDto(reminder);
    }

    @Override
    public ReminderResponseDto updateReminder(Long id, ReminderRequestDto dto) throws ResourceNotFoundException {
        log.info("Updating reminder ID {}", id);
        Reminder reminder = getReminderEntityById(id);

        if (dto.getRemindAt().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Reminder time must be in the future");
        }

        reminder.setRemindAt(dto.getRemindAt());
        reminder.setStatus(ReminderStatus.PENDING);
        reminder.setNotified(false);

        Reminder updatedReminder = reminderRepository.save(reminder);
        log.info("Reminder ID {} updated successfully", id);
        return entityMapper.toReminderResponseDto(updatedReminder);
    }

    @Override
    public void deleteReminder(Long id) throws ResourceNotFoundException {
        log.info("Deleting reminder ID {}", id);
        Reminder reminder = getReminderEntityById(id);
        reminderRepository.delete(reminder);
        log.info("Reminder ID {} deleted successfully", id);
    }

    @Override
    public ReminderResponseDto snoozeReminder(Long id, int minutes) throws ResourceNotFoundException {
        log.info("Snoozing reminder ID {} by {} minutes", id, minutes);
        Reminder reminder = getReminderEntityById(id);

        reminder.setRemindAt(LocalDateTime.now().plusMinutes(minutes));
        reminder.setStatus(ReminderStatus.SNOOZED);
        reminder.setNotified(false);

        Reminder updatedReminder = reminderRepository.save(reminder);
        log.info("Reminder ID {} snoozed successfully", id);
        return entityMapper.toReminderResponseDto(updatedReminder);
    }

    private Reminder getReminderEntityById(Long id) throws ResourceNotFoundException {
        Reminder reminder = reminderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorConstants.REMINDER_NOT_FOUND));

        User currentUser = userService.getAuthenticatedUser();
        verifyOwnershipOrCollaborator(reminder.getNote(), currentUser);
        return reminder;
    }

    private void verifyOwnershipOrCollaborator(Note note, User currentUser) {
        if (note.getOwner().getId().equals(currentUser.getId())) {
            return;
        }
        boolean isCollaborator = note.getCollaborators().stream()
                .anyMatch(c -> c.getUser().getId().equals(currentUser.getId()));
        if (!isCollaborator) {
            log.warn("Access denied: User {} has no rights on note {}", currentUser.getEmail(), note.getId());
            throw new AccessDeniedException(ErrorConstants.ACCESS_DENIED);
        }
    }
}
