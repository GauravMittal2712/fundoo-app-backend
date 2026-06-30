package com.bridgelabz.fundoo.service;

import com.bridgelabz.fundoo.dto.request.*;
import com.bridgelabz.fundoo.dto.response.*;
import com.bridgelabz.fundoo.exception.ResourceNotFoundException;

import java.util.List;

public interface CollaboratorService {
    CollaboratorResponseDto addCollaborator(Long noteId, CollaboratorRequestDto collaboratorRequest) throws ResourceNotFoundException;
    List<CollaboratorResponseDto> getCollaboratorsForNote(Long noteId) throws ResourceNotFoundException;
    CollaboratorResponseDto getCollaboratorById(Long id) throws ResourceNotFoundException;
    void removeCollaborator(Long noteId, Long collaboratorId) throws ResourceNotFoundException;
}
