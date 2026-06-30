package com.bridgelabz.fundoo.service;

import com.bridgelabz.fundoo.dto.request.*;
import com.bridgelabz.fundoo.dto.response.*;
import com.bridgelabz.fundoo.entity.Note;
import com.bridgelabz.fundoo.exception.ResourceNotFoundException;

import java.util.List;

public interface NoteService {
    NoteResponseDto createNote(NoteRequestDto noteRequest);
    NoteResponseDto getNoteById(Long id) throws ResourceNotFoundException;
    List<NoteResponseDto> getAllNotesForUser();
    NoteResponseDto updateNote(Long id, NoteRequestDto noteRequest) throws ResourceNotFoundException;
    void deleteNote(Long id) throws ResourceNotFoundException;
    List<NoteResponseDto> searchNotes(String query);
    NoteResponseDto togglePin(Long id) throws ResourceNotFoundException;
    NoteResponseDto toggleArchive(Long id) throws ResourceNotFoundException;
    NoteResponseDto toggleTrash(Long id) throws ResourceNotFoundException;
    NoteResponseDto updateColor(Long id, String color) throws ResourceNotFoundException;
    Note getNoteEntityById(Long id) throws ResourceNotFoundException;
}
