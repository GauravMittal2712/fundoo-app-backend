package com.bridgelabz.fundoo.service;

import com.bridgelabz.fundoo.dto.request.*;
import com.bridgelabz.fundoo.dto.response.*;
import com.bridgelabz.fundoo.exception.ResourceNotFoundException;

import java.util.List;

public interface ReminderService {
    ReminderResponseDto addReminderToNote(Long noteId, ReminderRequestDto reminderRequest) throws ResourceNotFoundException;
    List<ReminderResponseDto> getRemindersForNote(Long noteId) throws ResourceNotFoundException;
    ReminderResponseDto getReminderById(Long id) throws ResourceNotFoundException;
    ReminderResponseDto updateReminder(Long id, ReminderRequestDto reminderRequest) throws ResourceNotFoundException;
    void deleteReminder(Long id) throws ResourceNotFoundException;
    ReminderResponseDto snoozeReminder(Long id, int minutes) throws ResourceNotFoundException;
}
