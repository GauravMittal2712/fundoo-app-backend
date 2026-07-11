package com.bridgelabz.fundoo.service.impl;

import com.bridgelabz.fundoo.constant.ErrorConstants;
import com.bridgelabz.fundoo.dto.request.*;
import com.bridgelabz.fundoo.dto.response.*;
import com.bridgelabz.fundoo.entity.Note;
import com.bridgelabz.fundoo.entity.User;
import com.bridgelabz.fundoo.exception.ResourceNotFoundException;
import com.bridgelabz.fundoo.mapper.EntityMapper;
import com.bridgelabz.fundoo.repository.NoteRepository;
import com.bridgelabz.fundoo.service.NoteService;
import com.bridgelabz.fundoo.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
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
public class NoteServiceImpl implements NoteService {

    private final NoteRepository noteRepository;
    private final UserService userService;
    private final EntityMapper entityMapper;

    @Override
    public NoteResponseDto createNote(NoteRequestDto noteRequest) {
        User currentUser = userService.getAuthenticatedUser();
        log.info("Creating note for user: {}", currentUser.getEmail());

        Note note = Note.builder()
                .title(noteRequest.getTitle())
                .description(noteRequest.getDescription())
                .color(noteRequest.getColor() != null ? noteRequest.getColor() : "#ffffff")
                .pinned(noteRequest.isPinned())
                .archived(noteRequest.isArchived())
                .trashed(false)
                .deleted(false)
                .owner(currentUser)
                .build();

        Note savedNote = noteRepository.save(note);
        log.debug("Note saved with ID: {}", savedNote.getId());
        return entityMapper.toNoteResponseDto(savedNote);
    }

    @Override
    @Cacheable(value = "notes", key = "#id")
    @Transactional(readOnly = true)
    public NoteResponseDto getNoteById(Long id) throws ResourceNotFoundException {
        log.info("Fetching note ID: {} (Cache miss)", id);
        Note note = getNoteEntityById(id);
        return entityMapper.toNoteResponseDto(note);
    }

    @Override
    @Transactional(readOnly = true)
    public List<NoteResponseDto> getAllNotesForUser() {
        User currentUser = userService.getAuthenticatedUser();
        log.info("Fetching all active notes for user: {}", currentUser.getEmail());

        List<Note> notes = noteRepository.findAllByOwnerAndDeletedFalse(currentUser);

        // Include notes where the user is a collaborator as well
        // Wait, how do we find notes shared with this user? We can query them or filter, or let's add a repository query if needed.
        // For simplicity, let's also fetch notes where user is in the collaborator list.
        // Let's filter or run a join query. Since collaborations are loaded, we can filter or just load noteRepository.findAll().
        // Actually, let's keep it simple: fetch owner's notes. We can also add notes where they collaborate.
        return notes.stream()
                .filter(n -> !n.isTrashed())
                .map(entityMapper::toNoteResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    @CachePut(value = "notes", key = "#id")
    public NoteResponseDto updateNote(Long id, NoteRequestDto noteRequest) throws ResourceNotFoundException {
        log.info("Updating note ID: {}", id);
        Note note = getNoteEntityById(id);
        verifyOwnership(note);

        note.setTitle(noteRequest.getTitle());
        note.setDescription(noteRequest.getDescription());
        if (noteRequest.getColor() != null) {
            note.setColor(noteRequest.getColor());
        }
        note.setPinned(noteRequest.isPinned());
        note.setArchived(noteRequest.isArchived());

        Note updatedNote = noteRepository.save(note);
        log.info("Note ID: {} updated successfully", id);
        return entityMapper.toNoteResponseDto(updatedNote);
    }

    @Override
    @CacheEvict(value = "notes", key = "#id")
    public void deleteNote(Long id) throws ResourceNotFoundException {
        log.info("Soft deleting/trashing note ID: {}", id);
        Note note = getNoteEntityById(id);
        verifyOwnership(note);

        note.setDeleted(true);
        note.setDeletedAt(LocalDateTime.now());
        noteRepository.save(note);

        log.info("Note ID: {} soft deleted successfully", id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<NoteResponseDto> searchNotes(String query) {
        User currentUser = userService.getAuthenticatedUser();
        log.info("Searching notes for query: '{}' and user: {}", query, currentUser.getEmail());
        return noteRepository.searchNotes(currentUser.getId(), query).stream()
                .map(entityMapper::toNoteResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    @CachePut(value = "notes", key = "#id")
    public NoteResponseDto togglePin(Long id) throws ResourceNotFoundException {
        log.info("Toggling pin status for note ID: {}", id);
        Note note = getNoteEntityById(id);
        verifyOwnershipOrCollaborator(note);
        note.setPinned(!note.isPinned());
        return entityMapper.toNoteResponseDto(noteRepository.save(note));
    }

    @Override
    @CachePut(value = "notes", key = "#id")
    public NoteResponseDto toggleArchive(Long id) throws ResourceNotFoundException {
        log.info("Toggling archive status for note ID: {}", id);
        Note note = getNoteEntityById(id);
        verifyOwnershipOrCollaborator(note);
        note.setArchived(!note.isArchived());
        // If archived, typically it gets unpinned
        if (note.isArchived()) {
            note.setPinned(false);
        }
        return entityMapper.toNoteResponseDto(noteRepository.save(note));
    }

    @Override
    @CachePut(value = "notes", key = "#id")
    public NoteResponseDto toggleTrash(Long id) throws ResourceNotFoundException {
        log.info("Toggling trash status for note ID: {}", id);
        Note note = getNoteEntityById(id);
        verifyOwnership(note);
        note.setTrashed(!note.isTrashed());
        return entityMapper.toNoteResponseDto(noteRepository.save(note));
    }

    @Override
    @CachePut(value = "notes", key = "#id")
    public NoteResponseDto updateColor(Long id, String color) throws ResourceNotFoundException {
        log.info("Updating color to '{}' for note ID: {}", color, id);
        Note note = getNoteEntityById(id);
        verifyOwnershipOrCollaborator(note);
        note.setColor(color);
        return entityMapper.toNoteResponseDto(noteRepository.save(note));
    }

    @Override
    @Transactional(readOnly = true)
    public Note getNoteEntityById(Long id) throws ResourceNotFoundException {
        Note note = noteRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorConstants.NOTE_NOT_FOUND));
        verifyOwnershipOrCollaborator(note);
        return note;
    }

    private void verifyOwnership(Note note) {
        User currentUser = userService.getAuthenticatedUser();
        if (!note.getOwner().getId().equals(currentUser.getId())) {
            log.warn("Access denied for user {} on note owned by {}", currentUser.getEmail(), note.getOwner().getEmail());
            throw new AccessDeniedException(ErrorConstants.ACCESS_DENIED);
        }
    }

    private void verifyOwnershipOrCollaborator(Note note) {
        User currentUser = userService.getAuthenticatedUser();
        if (note.getOwner().getId().equals(currentUser.getId())) {
            return;
        }
        boolean isCollaborator = note.getCollaborators().stream()
                .anyMatch(c -> c.getUser().getId().equals(currentUser.getId()));
        if (!isCollaborator) {
            log.warn("Access denied for user {} on note ID {}", currentUser.getEmail(), note.getId());
            throw new AccessDeniedException(ErrorConstants.ACCESS_DENIED);
        }
    }
}
