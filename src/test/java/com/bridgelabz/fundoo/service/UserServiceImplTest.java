package com.bridgelabz.fundoo.service;

import com.bridgelabz.fundoo.dto.request.*;
import com.bridgelabz.fundoo.dto.response.*;
import com.bridgelabz.fundoo.entity.User;
import com.bridgelabz.fundoo.entity.enums.Role;
import com.bridgelabz.fundoo.exception.EmailAlreadyExistsException;
import com.bridgelabz.fundoo.exception.UserNotFoundException;
import com.bridgelabz.fundoo.mapper.EntityMapper;
import com.bridgelabz.fundoo.repository.UserRepository;
import com.bridgelabz.fundoo.service.impl.UserServiceImpl;
import com.bridgelabz.fundoo.util.JwtTokenProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private EntityMapper entityMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private ApplicationEventPublisher eventPublisher;

    @InjectMocks
    private UserServiceImpl userService;

    private User user;
    private RegisterRequestDto registerRequest;
    private UserResponseDto userResponse;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .password("hashed_password")
                .role(Role.ROLE_USER)
                .active(true)
                .verified(false)
                .deleted(false)
                .build();

        registerRequest = RegisterRequestDto.builder()
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .password("password123")
                .build();

        userResponse = UserResponseDto.builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .role(Role.ROLE_USER)
                .active(true)
                .verified(false)
                .build();
    }

    @Test
    void registerUser_Success() throws EmailAlreadyExistsException {
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(anyString())).thenReturn("hashed_password");
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(entityMapper.toUserResponseDto(any(User.class))).thenReturn(userResponse);

        UserResponseDto result = userService.registerUser(registerRequest);

        assertNotNull(result);
        assertEquals(userResponse.getEmail(), result.getEmail());
        verify(userRepository, times(1)).save(any(User.class));
        verify(eventPublisher, times(1)).publishEvent(any(User.class));
    }

    @Test
    void registerUser_EmailAlreadyExists_ThrowsException() {
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));

        assertThrows(EmailAlreadyExistsException.class, () -> userService.registerUser(registerRequest));
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void loginUser_Success() throws UserNotFoundException {
        LoginRequestDto loginRequest = new LoginRequestDto("john.doe@example.com", "password123");
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));
        when(jwtTokenProvider.generateToken(anyString())).thenReturn("jwt_token");

        AuthResponseDto result = userService.loginUser(loginRequest);

        assertNotNull(result);
        assertEquals("jwt_token", result.getToken());
        assertEquals("john.doe@example.com", result.getEmail());
        verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
    }

    @Test
    void getUserById_Success() throws UserNotFoundException {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(entityMapper.toUserResponseDto(user)).thenReturn(userResponse);

        UserResponseDto result = userService.getUserById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
    }

    @Test
    void getUserById_NotFound_ThrowsException() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.getUserById(1L));
    }
}
