package com.bridgelabz.fundoo.service;

import com.bridgelabz.fundoo.dto.request.*;
import com.bridgelabz.fundoo.dto.response.*;
import com.bridgelabz.fundoo.entity.User;
import com.bridgelabz.fundoo.exception.EmailAlreadyExistsException;
import com.bridgelabz.fundoo.exception.ResourceNotFoundException;
import com.bridgelabz.fundoo.exception.UserNotFoundException;

import java.util.List;

public interface UserService {
    UserResponseDto registerUser(RegisterRequestDto registerRequest) throws EmailAlreadyExistsException;
    AuthResponseDto loginUser(LoginRequestDto loginRequest) throws UserNotFoundException;
    UserResponseDto getUserById(Long id) throws UserNotFoundException;
    List<UserResponseDto> getAllUsers();
    UserResponseDto updateUser(Long id, RegisterRequestDto updateRequest) throws UserNotFoundException;
    void deleteUser(Long id) throws UserNotFoundException;
    void forgotPassword(ForgotPasswordDto forgotPasswordRequest) throws UserNotFoundException;
    void resetPassword(ResetPasswordDto resetPasswordRequest) throws ResourceNotFoundException;
    User getAuthenticatedUser();
}
