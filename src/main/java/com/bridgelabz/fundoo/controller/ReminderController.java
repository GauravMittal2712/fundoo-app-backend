package com.bridgelabz.fundoo.controller;

import com.bridgelabz.fundoo.dto.request.*;
import com.bridgelabz.fundoo.dto.response.*;
import com.bridgelabz.fundoo.response.APIResponse;
import com.bridgelabz.fundoo.service.ReminderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import com.bridgelabz.fundoo.constant.ApiConstants;
import com.bridgelabz.fundoo.constant.MessageConstants;
import com.bridgelabz.fundoo.constant.StatusConstants;

@RestController
@RequiredArgsConstructor
@Slf4j
public class ReminderController {

    private final ReminderService reminderService;

    @PostMapping(ApiConstants.NOTE_REMINDERS)
    public ResponseEntity<APIResponse<ReminderResponseDto>> addReminderToNote(
            @PathVariable Long noteId,
            @Valid @RequestBody ReminderRequestDto reminderRequest
    ) throws Exception {
        log.info("Received request to add reminder to note ID: {}", noteId);
        ReminderResponseDto response = reminderService.addReminderToNote(noteId, reminderRequest);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(APIResponse.success(StatusConstants.CREATED, MessageConstants.REMINDER_CREATED, response));
    }

    @GetMapping(ApiConstants.NOTE_REMINDERS)
    public ResponseEntity<APIResponse<List<ReminderResponseDto>>> getRemindersForNote(
            @PathVariable Long noteId
    ) throws Exception {
        log.info("Received request to fetch reminders for note ID: {}", noteId);
        List<ReminderResponseDto> response = reminderService.getRemindersForNote(noteId);
        return ResponseEntity.ok(APIResponse.success(StatusConstants.OK, MessageConstants.REMINDERS_FETCHED, response));
    }

    @GetMapping(ApiConstants.REMINDER_BY_ID)
    public ResponseEntity<APIResponse<ReminderResponseDto>> getReminderById(
            @PathVariable Long reminderId
    ) throws Exception {
        log.info("Received request to get reminder ID: {}", reminderId);
        ReminderResponseDto response = reminderService.getReminderById(reminderId);
        return ResponseEntity.ok(APIResponse.success(StatusConstants.OK, MessageConstants.REMINDER_FETCHED, response));
    }

    @PutMapping(ApiConstants.REMINDER_BY_ID)
    public ResponseEntity<APIResponse<ReminderResponseDto>> updateReminder(
            @PathVariable Long reminderId,
            @Valid @RequestBody ReminderRequestDto reminderRequest
    ) throws Exception {
        log.info("Received request to update reminder ID: {}", reminderId);
        ReminderResponseDto response = reminderService.updateReminder(reminderId, reminderRequest);
        return ResponseEntity.ok(APIResponse.success(StatusConstants.OK, MessageConstants.REMINDER_UPDATED, response));
    }

    @DeleteMapping(ApiConstants.REMINDER_BY_ID)
    public ResponseEntity<APIResponse<Void>> deleteReminder(
            @PathVariable Long reminderId
    ) throws Exception {
        log.info("Received request to delete reminder ID: {}", reminderId);
        reminderService.deleteReminder(reminderId);
        return ResponseEntity.ok(APIResponse.success(StatusConstants.OK, MessageConstants.REMINDER_DELETED));
    }

    @PatchMapping(ApiConstants.REMINDER_SNOOZE)
    public ResponseEntity<APIResponse<ReminderResponseDto>> snoozeReminder(
            @PathVariable Long reminderId,
            @RequestParam(defaultValue = "10") int minutes
    ) throws Exception {
        log.info("Received request to snooze reminder ID: {} by {} minutes", reminderId, minutes);
        ReminderResponseDto response = reminderService.snoozeReminder(reminderId, minutes);
        return ResponseEntity.ok(APIResponse.success(StatusConstants.OK, MessageConstants.REMINDER_SNOOZED, response));
    }
}
