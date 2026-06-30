package com.bridgelabz.fundoo.controller;

import com.bridgelabz.fundoo.dto.request.*;
import com.bridgelabz.fundoo.dto.response.*;
import com.bridgelabz.fundoo.response.APIResponse;
import com.bridgelabz.fundoo.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import com.bridgelabz.fundoo.constant.ApiConstants;
import com.bridgelabz.fundoo.constant.MessageConstants;
import com.bridgelabz.fundoo.constant.StatusConstants;

@RestController
@RequestMapping(ApiConstants.USERS)
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<APIResponse<UserResponseDto>> registerUser(
            @Valid @RequestBody RegisterRequestDto registerRequest
    ) throws Exception {
        log.info("Received request to register user: {}", registerRequest.getEmail());
        UserResponseDto response = userService.registerUser(registerRequest);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(APIResponse.success(StatusConstants.CREATED, MessageConstants.USER_REGISTERED, response));
    }

    @PostMapping("/login")
    public ResponseEntity<APIResponse<AuthResponseDto>> loginUser(
            @Valid @RequestBody LoginRequestDto loginRequest
    ) throws Exception {
        log.info("Received request to login user: {}", loginRequest.getEmail());
        AuthResponseDto response = userService.loginUser(loginRequest);
        return ResponseEntity.ok(APIResponse.success(StatusConstants.OK, MessageConstants.USER_LOGIN_SUCCESS, response));
    }

    @GetMapping("/{id}")
    public ResponseEntity<APIResponse<UserResponseDto>> getUserById(
            @PathVariable Long id
    ) throws Exception {
        log.info("Received request to fetch user ID: {}", id);
        UserResponseDto response = userService.getUserById(id);
        return ResponseEntity.ok(APIResponse.success(StatusConstants.OK, MessageConstants.USER_FETCHED, response));
    }

    @GetMapping
    public ResponseEntity<APIResponse<List<UserResponseDto>>> getAllUsers() {
        log.info("Received request to fetch all users");
        List<UserResponseDto> response = userService.getAllUsers();
        return ResponseEntity.ok(APIResponse.success(StatusConstants.OK, MessageConstants.USERS_FETCHED, response));
    }

    @PutMapping("/{id}")
    public ResponseEntity<APIResponse<UserResponseDto>> updateUser(
            @PathVariable Long id,
            @Valid @RequestBody RegisterRequestDto updateRequest
    ) throws Exception {
        log.info("Received request to update user ID: {}", id);
        UserResponseDto response = userService.updateUser(id, updateRequest);
        return ResponseEntity.ok(APIResponse.success(StatusConstants.OK, MessageConstants.USER_UPDATED, response));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<APIResponse<Void>> deleteUser(
            @PathVariable Long id
    ) throws Exception {
        log.info("Received request to delete user ID: {}", id);
        userService.deleteUser(id);
        return ResponseEntity.ok(APIResponse.success(StatusConstants.OK, MessageConstants.USER_DELETED));
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<APIResponse<Void>> forgotPassword(
            @Valid @RequestBody ForgotPasswordDto forgotPasswordRequest
    ) throws Exception {
        log.info("Received request for forgot password for email: {}", forgotPasswordRequest.getEmail());
        userService.forgotPassword(forgotPasswordRequest);
        return ResponseEntity.ok(APIResponse.success(StatusConstants.OK, MessageConstants.PASSWORD_RESET_SENT));
    }

    @PostMapping("/reset-password")
    public ResponseEntity<APIResponse<Void>> resetPassword(
            @Valid @RequestBody ResetPasswordDto resetPasswordRequest
    ) throws Exception {
        log.info("Received request to reset password");
        userService.resetPassword(resetPasswordRequest);
        return ResponseEntity.ok(APIResponse.success(StatusConstants.OK, MessageConstants.PASSWORD_RESET_SUCCESS));
    }
}
