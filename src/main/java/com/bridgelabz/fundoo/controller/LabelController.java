package com.bridgelabz.fundoo.controller;

import com.bridgelabz.fundoo.dto.request.*;
import com.bridgelabz.fundoo.dto.response.*;
import com.bridgelabz.fundoo.response.APIResponse;
import com.bridgelabz.fundoo.service.LabelService;
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
@RequestMapping(ApiConstants.LABELS)
@RequiredArgsConstructor
@Slf4j
public class LabelController {

    private final LabelService labelService;

    @PostMapping
    public ResponseEntity<APIResponse<LabelResponseDto>> createLabel(
            @Valid @RequestBody LabelRequestDto labelRequest
    ) {
        log.info("Received request to create label: {}", labelRequest.getName());
        LabelResponseDto response = labelService.createLabel(labelRequest);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(APIResponse.success(StatusConstants.CREATED, MessageConstants.LABEL_CREATED, response));
    }

    @GetMapping
    public ResponseEntity<APIResponse<List<LabelResponseDto>>> getAllLabels() {
        log.info("Received request to get all labels");
        List<LabelResponseDto> response = labelService.getAllLabelsForUser();
        return ResponseEntity.ok(APIResponse.success(StatusConstants.OK, MessageConstants.LABELS_FETCHED, response));
    }

    @GetMapping("/{labelId}")
    public ResponseEntity<APIResponse<LabelResponseDto>> getLabelById(
            @PathVariable Long labelId
    ) throws Exception {
        log.info("Received request to get label ID: {}", labelId);
        LabelResponseDto response = labelService.getLabelById(labelId);
        return ResponseEntity.ok(APIResponse.success(StatusConstants.OK, MessageConstants.LABEL_FETCHED, response));
    }

    @PutMapping("/{labelId}")
    public ResponseEntity<APIResponse<LabelResponseDto>> updateLabel(
            @PathVariable Long labelId,
            @Valid @RequestBody LabelRequestDto labelRequest
    ) throws Exception {
        log.info("Received request to update label ID: {}", labelId);
        LabelResponseDto response = labelService.updateLabel(labelId, labelRequest);
        return ResponseEntity.ok(APIResponse.success(StatusConstants.OK, MessageConstants.LABEL_UPDATED, response));
    }

    @DeleteMapping("/{labelId}")
    public ResponseEntity<APIResponse<Void>> deleteLabel(
            @PathVariable Long labelId
    ) throws Exception {
        log.info("Received request to delete label ID: {}", labelId);
        labelService.deleteLabel(labelId);
        return ResponseEntity.ok(APIResponse.success(StatusConstants.OK, MessageConstants.LABEL_DELETED));
    }

    @PostMapping("/notes/{noteId}/labels/{labelId}")
    public ResponseEntity<APIResponse<Void>> addLabelToNote(
            @PathVariable Long noteId,
            @PathVariable Long labelId
    ) throws Exception {
        log.info("Received request to add label ID {} to note ID {}", labelId, noteId);
        labelService.addLabelToNote(noteId, labelId);
        return ResponseEntity.ok(APIResponse.success(StatusConstants.OK, MessageConstants.LABEL_ADDED_TO_NOTE));
    }

    @DeleteMapping("/notes/{noteId}/labels/{labelId}")
    public ResponseEntity<APIResponse<Void>> removeLabelFromNote(
            @PathVariable Long noteId,
            @PathVariable Long labelId
    ) throws Exception {
        log.info("Received request to remove label ID {} from note ID {}", labelId, noteId);
        labelService.removeLabelFromNote(noteId, labelId);
        return ResponseEntity.ok(APIResponse.success(StatusConstants.OK, MessageConstants.LABEL_REMOVED_FROM_NOTE));
    }

    @GetMapping("/{labelId}/notes")
    public ResponseEntity<APIResponse<List<NoteResponseDto>>> getNotesByLabel(
            @PathVariable Long labelId
    ) throws Exception {
        log.info("Received request to get notes for label ID: {}", labelId);
        List<NoteResponseDto> response = labelService.getNotesByLabel(labelId);
        return ResponseEntity.ok(APIResponse.success(StatusConstants.OK, MessageConstants.LABEL_NOTES_FETCHED, response));
    }
}
