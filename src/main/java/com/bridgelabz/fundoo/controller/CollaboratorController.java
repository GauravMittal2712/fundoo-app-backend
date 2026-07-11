package com.bridgelabz.fundoo.controller;

import com.bridgelabz.fundoo.dto.request.*;
import com.bridgelabz.fundoo.dto.response.*;
import com.bridgelabz.fundoo.response.APIResponse;
import com.bridgelabz.fundoo.service.CollaboratorService;
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
public class CollaboratorController {

    private final CollaboratorService collaboratorService;

    @PostMapping(ApiConstants.NOTE_COLLABORATORS)
    public ResponseEntity<APIResponse<CollaboratorResponseDto>> addCollaborator(
            @PathVariable Long noteId,
            @Valid @RequestBody CollaboratorRequestDto collaboratorRequest
    ) throws Exception {
        log.info("Received request to add collaborator to note ID: {}", noteId);
        CollaboratorResponseDto response = collaboratorService.addCollaborator(noteId, collaboratorRequest);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(APIResponse.success(StatusConstants.CREATED, MessageConstants.COLLABORATOR_ADDED, response));
    }

    @GetMapping(ApiConstants.NOTE_COLLABORATORS)
    public ResponseEntity<APIResponse<List<CollaboratorResponseDto>>> getCollaboratorsForNote(
            @PathVariable Long noteId
    ) throws Exception {
        log.info("Received request to fetch collaborators for note ID: {}", noteId);
        List<CollaboratorResponseDto> response = collaboratorService.getCollaboratorsForNote(noteId);
        return ResponseEntity.ok(APIResponse.success(StatusConstants.OK, MessageConstants.COLLABORATORS_FETCHED, response));
    }

    @GetMapping(ApiConstants.COLLABORATOR_BY_ID)
    public ResponseEntity<APIResponse<CollaboratorResponseDto>> getCollaboratorById(
            @PathVariable Long collaboratorId
    ) throws Exception {
        log.info("Received request to get collaborator ID: {}", collaboratorId);
        CollaboratorResponseDto response = collaboratorService.getCollaboratorById(collaboratorId);
        return ResponseEntity.ok(APIResponse.success(StatusConstants.OK, MessageConstants.COLLABORATOR_FETCHED, response));
    }

    @DeleteMapping(ApiConstants.NOTE_COLLABORATOR_BY_ID)
    public ResponseEntity<APIResponse<Void>> removeCollaborator(
            @PathVariable Long noteId,
            @PathVariable Long collaboratorId
    ) throws Exception {
        log.info("Received request to remove collaborator ID: {} from note ID: {}", collaboratorId, noteId);
        collaboratorService.removeCollaborator(noteId, collaboratorId);
        return ResponseEntity.ok(APIResponse.success(StatusConstants.OK, MessageConstants.COLLABORATOR_REMOVED));
    }
}
