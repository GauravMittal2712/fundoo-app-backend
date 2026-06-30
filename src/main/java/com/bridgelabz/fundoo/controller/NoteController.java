package com.bridgelabz.fundoo.controller;

import com.bridgelabz.fundoo.dto.request.*;
import com.bridgelabz.fundoo.dto.response.*;
import com.bridgelabz.fundoo.response.APIResponse;
import com.bridgelabz.fundoo.service.NoteService;
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
@RequestMapping(ApiConstants.NOTES)
@RequiredArgsConstructor
@Slf4j
public class NoteController {

    private final NoteService noteService;

    @PostMapping
    public ResponseEntity<APIResponse<NoteResponseDto>> createNote(
            @Valid @RequestBody NoteRequestDto noteRequest
    ) {
        log.info("Received request to create note: {}", noteRequest.getTitle());
        NoteResponseDto response = noteService.createNote(noteRequest);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(APIResponse.success(StatusConstants.CREATED, MessageConstants.NOTE_CREATED, response));
    }

    @GetMapping
    public ResponseEntity<APIResponse<List<NoteResponseDto>>> getAllNotes() {
        log.info("Received request to get all notes for authenticated user");
        List<NoteResponseDto> response = noteService.getAllNotesForUser();
        return ResponseEntity.ok(APIResponse.success(StatusConstants.OK, MessageConstants.NOTES_FETCHED, response));
    }

    @GetMapping("/{noteId}")
    public ResponseEntity<APIResponse<NoteResponseDto>> getNoteById(
            @PathVariable Long noteId
    ) throws Exception {
        log.info("Received request to get note ID: {}", noteId);
        NoteResponseDto response = noteService.getNoteById(noteId);
        return ResponseEntity.ok(APIResponse.success(StatusConstants.OK, MessageConstants.NOTE_FETCHED, response));
    }

    @PutMapping("/{noteId}")
    public ResponseEntity<APIResponse<NoteResponseDto>> updateNote(
            @PathVariable Long noteId,
            @Valid @RequestBody NoteRequestDto noteRequest
    ) throws Exception {
        log.info("Received request to update note ID: {}", noteId);
        NoteResponseDto response = noteService.updateNote(noteId, noteRequest);
        return ResponseEntity.ok(APIResponse.success(StatusConstants.OK, MessageConstants.NOTE_UPDATED, response));
    }

    @DeleteMapping("/{noteId}")
    public ResponseEntity<APIResponse<Void>> deleteNote(
            @PathVariable Long noteId
    ) throws Exception {
        log.info("Received request to delete note ID: {}", noteId);
        noteService.deleteNote(noteId);
        return ResponseEntity.ok(APIResponse.success(StatusConstants.OK, MessageConstants.NOTE_DELETED));
    }

    @GetMapping("/search")
    public ResponseEntity<APIResponse<List<NoteResponseDto>>> searchNotes(
            @RequestParam String query
    ) {
        log.info("Received request to search notes with query: {}", query);
        List<NoteResponseDto> response = noteService.searchNotes(query);
        return ResponseEntity.ok(APIResponse.success(StatusConstants.OK, MessageConstants.NOTES_SEARCHED, response));
    }

    @PatchMapping("/{noteId}/pin")
    public ResponseEntity<APIResponse<NoteResponseDto>> togglePin(
            @PathVariable Long noteId
    ) throws Exception {
        log.info("Received request to toggle pin for note ID: {}", noteId);
        NoteResponseDto response = noteService.togglePin(noteId);
        return ResponseEntity.ok(APIResponse.success(StatusConstants.OK, MessageConstants.NOTE_PINNED, response));
    }

    @PatchMapping("/{noteId}/archive")
    public ResponseEntity<APIResponse<NoteResponseDto>> toggleArchive(
            @PathVariable Long noteId
    ) throws Exception {
        log.info("Received request to toggle archive for note ID: {}", noteId);
        NoteResponseDto response = noteService.toggleArchive(noteId);
        return ResponseEntity.ok(APIResponse.success(StatusConstants.OK, MessageConstants.NOTE_ARCHIVED, response));
    }

    @PatchMapping("/{noteId}/trash")
    public ResponseEntity<APIResponse<NoteResponseDto>> toggleTrash(
            @PathVariable Long noteId
    ) throws Exception {
        log.info("Received request to toggle trash status for note ID: {}", noteId);
        NoteResponseDto response = noteService.toggleTrash(noteId);
        return ResponseEntity.ok(APIResponse.success(StatusConstants.OK, MessageConstants.NOTE_TRASHED, response));
    }

    @PatchMapping("/{noteId}/color")
    public ResponseEntity<APIResponse<NoteResponseDto>> updateColor(
            @PathVariable Long noteId,
            @RequestParam String color
    ) throws Exception {
        log.info("Received request to update color for note ID: {} to {}", noteId, color);
        NoteResponseDto response = noteService.updateColor(noteId, color);
        return ResponseEntity.ok(APIResponse.success(StatusConstants.OK, MessageConstants.NOTE_COLOR_UPDATED, response));
    }
}
