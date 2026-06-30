package com.bridgelabz.fundoo.service.impl;

import com.bridgelabz.fundoo.constant.ErrorConstants;
import com.bridgelabz.fundoo.dto.request.*;
import com.bridgelabz.fundoo.dto.response.*;
import com.bridgelabz.fundoo.entity.Collaborator;
import com.bridgelabz.fundoo.entity.Note;
import com.bridgelabz.fundoo.entity.User;
import com.bridgelabz.fundoo.entity.enums.CollaboratorRole;
import com.bridgelabz.fundoo.exception.ResourceNotFoundException;
import com.bridgelabz.fundoo.mapper.EntityMapper;
import com.bridgelabz.fundoo.repository.CollaboratorRepository;
import com.bridgelabz.fundoo.repository.UserRepository;
import com.bridgelabz.fundoo.service.CollaboratorService;
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
public class CollaboratorServiceImpl implements CollaboratorService {

    private final CollaboratorRepository collaboratorRepository;
    private final NoteService noteService;
    private final UserService userService;
    private final UserRepository userRepository;
    private final EntityMapper entityMapper;

    @Override
    public CollaboratorResponseDto addCollaborator(Long noteId, CollaboratorRequestDto dto) throws ResourceNotFoundException {
        User currentUser = userService.getAuthenticatedUser();
        log.info("User {} adding collaborator {} to note ID {}", currentUser.getEmail(), dto.getEmail(), noteId);

        Note note = noteService.getNoteEntityById(noteId);
        verifyOwnership(note, currentUser);

        if (note.getOwner().getEmail().equalsIgnoreCase(dto.getEmail())) {
            throw new IllegalArgumentException("Owners cannot collaborate with themselves");
        }

        User collaboratorUser = userRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("Collaborator user not found with email: " + dto.getEmail()));

        // Check if collaboration already exists
        collaboratorRepository.findByNoteAndUser(note, collaboratorUser)
                .ifPresent(c -> {
                    throw new IllegalArgumentException("User is already a collaborator on this note");
                });

        Collaborator collaborator = Collaborator.builder()
                .note(note)
                .user(collaboratorUser)
                .role(dto.getRole() != null ? dto.getRole() : CollaboratorRole.EDITOR)
                .build();

        Collaborator savedCollaborator = collaboratorRepository.save(collaborator);
        log.info("Added collaborator successfully with ID {}", savedCollaborator.getId());
        return entityMapper.toCollaboratorResponseDto(savedCollaborator);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CollaboratorResponseDto> getCollaboratorsForNote(Long noteId) throws ResourceNotFoundException {
        log.info("Fetching collaborators for note ID {}", noteId);
        Note note = noteService.getNoteEntityById(noteId);
        return collaboratorRepository.findAllByNote(note).stream()
                .map(entityMapper::toCollaboratorResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public CollaboratorResponseDto getCollaboratorById(Long id) throws ResourceNotFoundException {
        log.info("Fetching collaborator by ID {}", id);
        Collaborator collaborator = collaboratorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorConstants.COLLABORATOR_NOT_FOUND));

        User currentUser = userService.getAuthenticatedUser();
        // Authorized if owner of note or the collaborator user themselves
        if (!collaborator.getNote().getOwner().getId().equals(currentUser.getId()) &&
            !collaborator.getUser().getId().equals(currentUser.getId())) {
            throw new AccessDeniedException(ErrorConstants.ACCESS_DENIED);
        }

        return entityMapper.toCollaboratorResponseDto(collaborator);
    }

    @Override
    public void removeCollaborator(Long noteId, Long collaboratorId) throws ResourceNotFoundException {
        User currentUser = userService.getAuthenticatedUser();
        log.info("User {} removing collaborator ID {} from note ID {}", currentUser.getEmail(), collaboratorId, noteId);

        Note note = noteService.getNoteEntityById(noteId);
        verifyOwnership(note, currentUser);

        Collaborator collaborator = collaboratorRepository.findById(collaboratorId)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorConstants.COLLABORATOR_NOT_FOUND));

        if (!collaborator.getNote().getId().equals(noteId)) {
            throw new IllegalArgumentException("Collaborator does not belong to the specified note");
        }

        collaboratorRepository.delete(collaborator);
        log.info("Removed collaborator successfully");
    }

    private void verifyOwnership(Note note, User currentUser) {
        if (!note.getOwner().getId().equals(currentUser.getId())) {
            log.warn("Access denied: User {} is not the owner of note {}", currentUser.getEmail(), note.getId());
            throw new AccessDeniedException(ErrorConstants.ACCESS_DENIED);
        }
    }
}
