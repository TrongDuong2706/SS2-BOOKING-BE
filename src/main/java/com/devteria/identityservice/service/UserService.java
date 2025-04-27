package com.devteria.identityservice.service;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.devteria.identityservice.dto.request.*;
import com.devteria.identityservice.dto.response.ChangePasswordResponse;
import com.devteria.identityservice.dto.response.UpdateAvatarResponse;
import com.devteria.identityservice.util.QrCodeUtil;
import com.google.zxing.WriterException;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.devteria.identityservice.constant.PredefinedRole;
import com.devteria.identityservice.dto.response.UserResponse;
import com.devteria.identityservice.entity.Role;
import com.devteria.identityservice.entity.User;
import com.devteria.identityservice.exception.AppException;
import com.devteria.identityservice.exception.ErrorCode;
import com.devteria.identityservice.mapper.UserMapper;
import com.devteria.identityservice.repository.RoleRepository;
import com.devteria.identityservice.repository.UserRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class UserService {
    UserRepository userRepository;
    RoleRepository roleRepository;
    UserMapper userMapper;
    PasswordEncoder passwordEncoder;
    Cloudinary cloudinary;

    public UserResponse createUser(UserCreationRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) throw new AppException(ErrorCode.USER_EXISTED);

        User user = userMapper.toUser(request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        HashSet<Role> roles = new HashSet<>();
        roleRepository.findById(PredefinedRole.USER_ROLE).ifPresent(roles::add);

        user.setRoles(roles);
        User savedUser = userRepository.save(user);
        UserResponse userResponse = userMapper.toUserResponse(savedUser);
        try {
            // Serialize thông tin UserResponse thành JSON
            String userJson = QrCodeUtil.prettyObject(request);

            // Tạo QR code từ JSON
            String qrCode = QrCodeUtil.generateQrCode(userJson, 200, 200);

            // Gán QR code vào response
            userResponse.setQrCode(qrCode);
        } catch (WriterException | IOException e) {
            e.printStackTrace();
            // Xử lý nếu có lỗi khi tạo QR code
//            throw new AppException(ErrorCode.QR_CODE_GENERATION_FAILED);
        }

        return userResponse;
    }

    public void createPassword(PasswordCreationRequest request) {
        //Để biết user này là user nào, sử dụng SecurityContextHolder
        var context = SecurityContextHolder.getContext();
        String name = context.getAuthentication().getName();
        User user = userRepository.findByUsername(name).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        //Nếu có password thì:
        if (StringUtils.hasText(user.getPassword()))
            throw new AppException(ErrorCode.PASSWORD_EXISTED);
        //if not set password
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        userRepository.save(user);
    }

    public UserResponse getMyInfo() {
        var context = SecurityContextHolder.getContext();
        String name = context.getAuthentication().getName();
        log.info("Authenticated username: {}", name);
        User user = userRepository.findByUsername(name).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        var userResponse = userMapper.toUserResponse(user);
        //Nếu user  có password -> false,nếu không có password -> true
        userResponse.setNoPassword(!StringUtils.hasText(user.getPassword()));
        return userResponse;
    }

    //    @PostAuthorize("returnObject.username == authentication.name")
    public UserResponse updateUser(String userId, UserUpdateRequest request) {
        User user = userRepository.findById(userId).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        userMapper.updateUser(user, request);
//        user.setPassword(passwordEncoder.encode(request.getPassword()));
//        var roles = roleRepository.findAllById(request.getRoles());
//        user.setRoles(new HashSet<>(roles));
        return userMapper.toUserResponse(userRepository.save(user));
    }

    @PreAuthorize("hasRole('ADMIN')")
    public void deleteUser(String userId) {
        userRepository.deleteById(userId);
    }

    //    @PreAuthorize("hasRole('ADMIN')")
    public List<UserResponse> getUsers() {
        log.info("In method get Users");
        return userRepository.findAll().stream().map(userMapper::toUserResponse).toList();
    }

    @PreAuthorize("hasRole('ADMIN')")
    public UserResponse getUser(String id) {
        return userMapper.toUserResponse(
                userRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED)));
    }

    //API đổi avatar
    public UpdateAvatarResponse updateAvatar(String userId, MultipartFile file) {
        var user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        String url;
        try {
            // Tải ảnh lên Cloudinary và lấy URL của ảnh
            Map<String, Object> uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());
            url = (String) uploadResult.get("url");
            user.setAvatar(url);
        } catch (IOException e) {
            throw new RuntimeException("Failed to upload avatar image", e);
        }
        userRepository.save(user);

        return UpdateAvatarResponse.builder()
                .avatar(url)
                .build();
    }

    //Api đổi mật khẩu
    public ChangePasswordResponse changePassword(String userId, ChangePasswordRequest request) {
        var user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new AppException(ErrorCode.PASSWORD_NOT_CORRECT);
        }
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
        return ChangePasswordResponse.builder()
                .password(user.getPassword())
                .build();
    }

    //Đếm tổng user
    public long getTotalUser() {
        return userRepository.countTotalUser();
    }

}
