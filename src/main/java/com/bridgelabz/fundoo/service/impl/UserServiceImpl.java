package com.bridgelabz.fundoo.service.impl;

import com.bridgelabz.fundoo.constant.ErrorConstants;
import com.bridgelabz.fundoo.dto.request.*;
import com.bridgelabz.fundoo.dto.response.*;
import com.bridgelabz.fundoo.entity.PasswordResetToken;
import com.bridgelabz.fundoo.entity.User;
import com.bridgelabz.fundoo.entity.enums.Role;
import com.bridgelabz.fundoo.exception.EmailAlreadyExistsException;
import com.bridgelabz.fundoo.exception.ResourceNotFoundException;
import com.bridgelabz.fundoo.exception.UserNotFoundException;
import com.bridgelabz.fundoo.mapper.EntityMapper;
import com.bridgelabz.fundoo.repository.PasswordResetTokenRepository;
import com.bridgelabz.fundoo.repository.UserRepository;
import com.bridgelabz.fundoo.service.UserService;
import com.bridgelabz.fundoo.util.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final EntityMapper entityMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManager authenticationManager;
    private final ApplicationEventPublisher eventPublisher;

    @Override
    public UserResponseDto registerUser(RegisterRequestDto dto) throws EmailAlreadyExistsException {
        log.info("Registering new user with email: {}", dto.getEmail());

        if (userRepository.findByEmail(dto.getEmail()).isPresent()) {
            log.warn("Registration failed - email {} already exists", dto.getEmail());
            throw new EmailAlreadyExistsException(ErrorConstants.EMAIL_ALREADY_EXISTS);
        }

        User user = User.builder()
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .email(dto.getEmail())
                .password(passwordEncoder.encode(dto.getPassword()))
                .phoneNumber(dto.getPhoneNumber())
                .role(Role.ROLE_USER)
                .active(true)
                .verified(false)
                .deleted(false)
                .build();

        User savedUser = userRepository.save(user);
        log.debug("Saved user to database with ID: {}", savedUser.getId());

        // Publish event for Kafka / Async processing
        eventPublisher.publishEvent(savedUser);

        return entityMapper.toUserResponseDto(savedUser);
    }

    @Override
    public AuthResponseDto loginUser(LoginRequestDto dto) throws UserNotFoundException {
        log.info("Attempting login for email: {}", dto.getEmail());

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(dto.getEmail(), dto.getPassword())
        );

        User user = userRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new UserNotFoundException(ErrorConstants.USER_NOT_FOUND));

        user.setLastLoginAt(LocalDateTime.now());
        userRepository.save(user);

        String token = jwtTokenProvider.generateToken(user.getEmail());
        log.info("Login successful for email: {}, generated JWT", dto.getEmail());

        return AuthResponseDto.builder()
                .token(token)
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .build();
    }

    @Override
    @Cacheable(value = "users", key = "#id")
    @Transactional(readOnly = true)
    public UserResponseDto getUserById(Long id) throws UserNotFoundException {
        log.info("Fetching user details for ID: {} (Cache miss)", id);
        User user = userRepository.findById(id)
                .filter(u -> !u.isDeleted())
                .orElseThrow(() -> new UserNotFoundException(ErrorConstants.USER_NOT_FOUND));
        return entityMapper.toUserResponseDto(user);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserResponseDto> getAllUsers() {
        log.info("Fetching all active non-deleted users");
        return userRepository.findAll().stream()
                .filter(u -> !u.isDeleted())
                .map(entityMapper::toUserResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    @CachePut(value = "users", key = "#id")
    public UserResponseDto updateUser(Long id, RegisterRequestDto dto) throws UserNotFoundException {
        log.info("Updating user details for ID: {}", id);

        User user = userRepository.findById(id)
                .filter(u -> !u.isDeleted())
                .orElseThrow(() -> new UserNotFoundException(ErrorConstants.USER_NOT_FOUND));

        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setPhoneNumber(dto.getPhoneNumber());

        if (dto.getPassword() != null && !dto.getPassword().isBlank()) {
            user.setPassword(passwordEncoder.encode(dto.getPassword()));
        }

        User updatedUser = userRepository.save(user);
        log.info("User ID: {} updated successfully", id);

        return entityMapper.toUserResponseDto(updatedUser);
    }

    @Override
    @CacheEvict(value = "users", key = "#id")
    public void deleteUser(Long id) throws UserNotFoundException {
        log.info("Soft deleting user with ID: {}", id);

        User user = userRepository.findById(id)
                .filter(u -> !u.isDeleted())
                .orElseThrow(() -> new UserNotFoundException(ErrorConstants.USER_NOT_FOUND));

        user.setDeleted(true);
        user.setDeletedAt(LocalDateTime.now());
        user.setActive(false);
        userRepository.save(user);

        log.info("User ID: {} soft deleted", id);
    }

    @Override
    public void forgotPassword(ForgotPasswordDto dto) throws UserNotFoundException {
        log.info("Requesting password reset for email: {}", dto.getEmail());

        User user = userRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new UserNotFoundException(ErrorConstants.USER_NOT_FOUND));

        String token = UUID.randomUUID().toString();
        PasswordResetToken resetToken = PasswordResetToken.builder()
                .token(token)
                .user(user)
                .expiryDate(LocalDateTime.now().plusHours(2))
                .revoked(false)
                .build();

        passwordResetTokenRepository.save(resetToken);
        log.info("Generated password reset token for email: {}", dto.getEmail());

        // Publish event for sending email asynchronously (will print to logs or kafka)
        eventPublisher.publishEvent(resetToken);
    }

    @Override
    public void resetPassword(ResetPasswordDto dto) throws ResourceNotFoundException {
        log.info("Executing password reset for token");

        PasswordResetToken resetToken = passwordResetTokenRepository.findByToken(dto.getToken())
                .orElseThrow(() -> new ResourceNotFoundException("Invalid or expired password reset token"));

        if (!resetToken.isValid()) {
            log.warn("Password reset attempt failed - token is expired or revoked");
            throw new ResourceNotFoundException("Password reset token is expired or revoked");
        }

        User user = resetToken.getUser();
        user.setPassword(passwordEncoder.encode(dto.getNewPassword()));
        userRepository.save(user);

        resetToken.setRevoked(true);
        passwordResetTokenRepository.save(resetToken);

        log.info("Password reset successful for user ID: {}", user.getId());
    }

    @Override
    @CachePut(value = "users", key = "#id")
    @Transactional
    public UserResponseDto updateUserRole(Long id, Role role) throws UserNotFoundException {
        log.info("Updating user role for ID: {} to {}", id, role);
        User user = userRepository.findById(id)
                .filter(u -> !u.isDeleted())
                .orElseThrow(() -> new UserNotFoundException(ErrorConstants.USER_NOT_FOUND));
        user.setRole(role);
        User savedUser = userRepository.save(user);
        return entityMapper.toUserResponseDto(savedUser);
    }

    @Override
    @Transactional(readOnly = true)
    public User getAuthenticatedUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Current authenticated user not found in database: " + email));
    }
}
