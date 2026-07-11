package com.bridgelabz.fundoo.service;

import com.bridgelabz.fundoo.dto.request.*;
import com.bridgelabz.fundoo.dto.response.*;
import com.bridgelabz.fundoo.exception.ResourceNotFoundException;

import java.util.List;

public interface LabelService {
    LabelResponseDto createLabel(LabelRequestDto labelRequest);
    LabelResponseDto getLabelById(Long id) throws ResourceNotFoundException;
    List<LabelResponseDto> getAllLabelsForUser();
    LabelResponseDto updateLabel(Long id, LabelRequestDto labelRequest) throws ResourceNotFoundException;
    void deleteLabel(Long id) throws ResourceNotFoundException;
    void addLabelToNote(Long noteId, Long labelId) throws ResourceNotFoundException;
    void removeLabelFromNote(Long noteId, Long labelId) throws ResourceNotFoundException;
    List<NoteResponseDto> getNotesByLabel(Long labelId) throws ResourceNotFoundException;
}
