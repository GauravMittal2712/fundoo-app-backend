package com.bridgelabz.fundoo.service;

import com.bridgelabz.fundoo.dto.request.*;
import com.bridgelabz.fundoo.dto.response.*;
import com.bridgelabz.fundoo.entity.Note;
import com.bridgelabz.fundoo.entity.User;
import com.bridgelabz.fundoo.exception.ResourceNotFoundException;
import com.bridgelabz.fundoo.mapper.EntityMapper;
import com.bridgelabz.fundoo.repository.NoteRepository;
import com.bridgelabz.fundoo.service.impl.NoteServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.access.AccessDeniedException;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class NoteServiceImplTest {

    @Mock
    private NoteRepository noteRepository;

    @Mock
    private UserService userService;

    @Mock
    private EntityMapper entityMapper;

    @InjectMocks
    private NoteServiceImpl noteService;

    private User user;
    private User otherUser;
    private Note note;
    private NoteRequestDto noteRequest;
    private NoteResponseDto noteResponse;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .id(1L)
                .email("owner@example.com")
                .build();

        otherUser = User.builder()
                .id(2L)
                .email("other@example.com")
                .build();

        note = Note.builder()
                .id(10L)
                .title("Test Note")
                .description("Description")
                .color("#ffffff")
                .owner(user)
                .collaborators(Collections.emptyList())
                .build();

        noteRequest = NoteRequestDto.builder()
                .title("Updated Note")
                .description("Updated Description")
                .build();

        noteResponse = NoteResponseDto.builder()
                .id(10L)
                .title("Updated Note")
                .description("Updated Description")
                .ownerId(1L)
                .build();
    }

    @Test
    void createNote_Success() {
        when(userService.getAuthenticatedUser()).thenReturn(user);
        when(noteRepository.save(any(Note.class))).thenReturn(note);
        when(entityMapper.toNoteResponseDto(any(Note.class))).thenReturn(noteResponse);

        NoteResponseDto result = noteService.createNote(noteRequest);

        assertNotNull(result);
        assertEquals("Updated Note", result.getTitle());
        verify(noteRepository, times(1)).save(any(Note.class));
    }

    @Test
    void getNoteById_Success() throws ResourceNotFoundException {
        when(userService.getAuthenticatedUser()).thenReturn(user);
        when(noteRepository.findByIdAndDeletedFalse(10L)).thenReturn(Optional.of(note));
        when(entityMapper.toNoteResponseDto(note)).thenReturn(noteResponse);

        NoteResponseDto result = noteService.getNoteById(10L);

        assertNotNull(result);
        assertEquals(10L, result.getId());
    }

    @Test
    void getNoteById_AccessDenied_ThrowsException() {
        when(userService.getAuthenticatedUser()).thenReturn(otherUser);
        when(noteRepository.findByIdAndDeletedFalse(10L)).thenReturn(Optional.of(note));

        assertThrows(AccessDeniedException.class, () -> noteService.getNoteById(10L));
    }

    @Test
    void updateNote_Success() throws ResourceNotFoundException {
        when(userService.getAuthenticatedUser()).thenReturn(user);
        when(noteRepository.findByIdAndDeletedFalse(10L)).thenReturn(Optional.of(note));
        when(noteRepository.save(any(Note.class))).thenReturn(note);
        when(entityMapper.toNoteResponseDto(any(Note.class))).thenReturn(noteResponse);

        NoteResponseDto result = noteService.updateNote(10L, noteRequest);

        assertNotNull(result);
        verify(noteRepository, times(1)).save(any(Note.class));
    }

    @Test
    void deleteNote_Success() throws ResourceNotFoundException {
        when(userService.getAuthenticatedUser()).thenReturn(user);
        when(noteRepository.findByIdAndDeletedFalse(10L)).thenReturn(Optional.of(note));

        noteService.deleteNote(10L);

        assertTrue(note.isDeleted());
        assertNotNull(note.getDeletedAt());
        verify(noteRepository, times(1)).save(note);
    }
}
