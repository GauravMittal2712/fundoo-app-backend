package com.bridgelabz.fundoo.service.impl;

import com.bridgelabz.fundoo.constant.ErrorConstants;
import com.bridgelabz.fundoo.dto.request.*;
import com.bridgelabz.fundoo.dto.response.*;
import com.bridgelabz.fundoo.entity.Label;
import com.bridgelabz.fundoo.entity.Note;
import com.bridgelabz.fundoo.entity.User;
import com.bridgelabz.fundoo.exception.ResourceNotFoundException;
import com.bridgelabz.fundoo.mapper.EntityMapper;
import com.bridgelabz.fundoo.repository.LabelRepository;
import com.bridgelabz.fundoo.repository.NoteRepository;
import com.bridgelabz.fundoo.service.LabelService;
import com.bridgelabz.fundoo.service.NoteService;
import com.bridgelabz.fundoo.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class LabelServiceImpl implements LabelService {

    private final LabelRepository labelRepository;
    private final NoteRepository noteRepository;
    private final NoteService noteService;
    private final UserService userService;
    private final EntityMapper entityMapper;

    @Override
    public LabelResponseDto createLabel(LabelRequestDto labelRequest) {
        User currentUser = userService.getAuthenticatedUser();
        log.info("Creating label '{}' for user: {}", labelRequest.getName(), currentUser.getEmail());

        // Check if label already exists
        labelRepository.findByNameAndUser(labelRequest.getName(), currentUser)
                .ifPresent(l -> {
                    throw new IllegalArgumentException("Label with name '" + labelRequest.getName() + "' already exists");
                });

        Label label = Label.builder()
                .name(labelRequest.getName())
                .user(currentUser)
                .build();

        Label savedLabel = labelRepository.save(label);
        log.debug("Label saved with ID: {}", savedLabel.getId());
        return entityMapper.toLabelResponseDto(savedLabel);
    }

    @Override
    @Transactional(readOnly = true)
    public LabelResponseDto getLabelById(Long id) throws ResourceNotFoundException {
        log.info("Fetching label ID: {}", id);
        Label label = getLabelEntityById(id);
        return entityMapper.toLabelResponseDto(label);
    }

    @Override
    @Transactional(readOnly = true)
    public List<LabelResponseDto> getAllLabelsForUser() {
        User currentUser = userService.getAuthenticatedUser();
        log.info("Fetching all labels for user: {}", currentUser.getEmail());
        return labelRepository.findAllByUser(currentUser).stream()
                .map(entityMapper::toLabelResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public LabelResponseDto updateLabel(Long id, LabelRequestDto labelRequest) throws ResourceNotFoundException {
        log.info("Updating label ID: {}", id);
        Label label = getLabelEntityById(id);

        // Check for duplicates on rename
        User currentUser = userService.getAuthenticatedUser();
        labelRepository.findByNameAndUser(labelRequest.getName(), currentUser)
                .ifPresent(l -> {
                    if (!l.getId().equals(id)) {
                        throw new IllegalArgumentException("Label with name '" + labelRequest.getName() + "' already exists");
                    }
                });

        label.setName(labelRequest.getName());
        Label updatedLabel = labelRepository.save(label);
        log.info("Label ID: {} updated successfully", id);
        return entityMapper.toLabelResponseDto(updatedLabel);
    }

    @Override
    public void deleteLabel(Long id) throws ResourceNotFoundException {
        log.info("Deleting label ID: {}", id);
        Label label = getLabelEntityById(id);

        // Remove label from all associated notes before deletion to prevent constraint violations
        label.getNotes().forEach(note -> note.getLabels().remove(label));
        labelRepository.delete(label);

        log.info("Label ID: {} deleted successfully", id);
    }

    @Override
    public void addLabelToNote(Long noteId, Long labelId) throws ResourceNotFoundException {
        log.info("Adding label ID: {} to note ID: {}", labelId, noteId);
        Note note = noteService.getNoteEntityById(noteId);
        Label label = getLabelEntityById(labelId);

        note.getLabels().add(label);
        noteRepository.save(note);
        log.info("Label ID: {} successfully added to note ID: {}", labelId, noteId);
    }

    @Override
    public void removeLabelFromNote(Long noteId, Long labelId) throws ResourceNotFoundException {
        log.info("Removing label ID: {} from note ID: {}", labelId, noteId);
        Note note = noteService.getNoteEntityById(noteId);
        Label label = getLabelEntityById(labelId);

        note.getLabels().remove(label);
        noteRepository.save(note);
        log.info("Label ID: {} successfully removed from note ID: {}", labelId, noteId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<NoteResponseDto> getNotesByLabel(Long labelId) throws ResourceNotFoundException {
        log.info("Fetching all notes for label ID: {}", labelId);
        Label label = getLabelEntityById(labelId);

        return label.getNotes().stream()
                .filter(n -> !n.isDeleted() && !n.isTrashed())
                .map(entityMapper::toNoteResponseDto)
                .collect(Collectors.toList());
    }

    private Label getLabelEntityById(Long id) throws ResourceNotFoundException {
        User currentUser = userService.getAuthenticatedUser();
        Label label = labelRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorConstants.LABEL_NOT_FOUND));

        if (!label.getUser().getId().equals(currentUser.getId())) {
            log.warn("Access denied for user {} on label ID {}", currentUser.getEmail(), id);
            throw new AccessDeniedException(ErrorConstants.ACCESS_DENIED);
        }
        return label;
    }
}
