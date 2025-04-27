package com.devteria.identityservice.controller;

import java.util.List;

import com.devteria.identityservice.dto.request.*;
import com.devteria.identityservice.dto.response.ChangePasswordResponse;
import com.devteria.identityservice.dto.response.UpdateAvatarResponse;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.*;

import com.devteria.identityservice.dto.response.UserResponse;
import com.devteria.identityservice.service.UserService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class UserController {
    UserService userService;
    @PostMapping("/create-password")
    ApiResponse<Void> createPassword(@RequestBody @Valid PasswordCreationRequest request) {
        userService.createPassword(request);
        return ApiResponse.<Void>builder()
                .message("Password has been created, you could use it to login")
                .build();
    }
    @PostMapping
    ApiResponse<UserResponse> createUser(@RequestBody @Valid UserCreationRequest request) {
        return ApiResponse.<UserResponse>builder()
                .result(userService.createUser(request))
                .build();
    }

    @GetMapping
    ApiResponse<List<UserResponse>> getUsers() {
        return ApiResponse.<List<UserResponse>>builder()
                .result(userService.getUsers())
                .build();
    }

    @GetMapping("/{userId}")
    ApiResponse<UserResponse> getUser(@PathVariable("userId") String userId) {
        return ApiResponse.<UserResponse>builder()
                .result(userService.getUser(userId))
                .build();
    }

    @GetMapping("/myInfo")
    ApiResponse<UserResponse> getMyInfo() {
        return ApiResponse.<UserResponse>builder()
                .result(userService.getMyInfo())
                .build();
    }

    @DeleteMapping("/{userId}")
    ApiResponse<String> deleteUser(@PathVariable String userId) {
        userService.deleteUser(userId);
        return ApiResponse.<String>builder().result("User has been deleted").build();
    }

    @PutMapping("/{userId}")
    ApiResponse<UserResponse> updateUser(@PathVariable String userId, @RequestBody UserUpdateRequest request) {
        return ApiResponse.<UserResponse>builder()
                .result(userService.updateUser(userId, request))
                .build();
    }
    @PostMapping("/avatar/{userId}")
    ApiResponse<UpdateAvatarResponse> updateUserAvatar(@PathVariable String userId, MultipartFile file) {
        return ApiResponse.<UpdateAvatarResponse>builder()
                .result(userService.updateAvatar(userId, file))
                .build();
    }
    @PostMapping("password/{userId}")
    ApiResponse<ChangePasswordResponse> changePassword(
            @PathVariable String userId, @RequestBody ChangePasswordRequest request){
        return ApiResponse.<ChangePasswordResponse>builder()
                .result(userService.changePassword(userId, request))
                .build();
    }
    @GetMapping("/totalUsers")
    ApiResponse<Long> getTotalUser(){
        return ApiResponse.<Long>builder()
                .result(userService.getTotalUser())
                .build();
    }

}
