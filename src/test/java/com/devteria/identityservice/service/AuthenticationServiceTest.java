package com.devteria.identityservice.service;

import com.devteria.identityservice.dto.request.AuthenticationRequest;
import com.devteria.identityservice.dto.response.AuthenticationResponse;
import com.devteria.identityservice.entity.User;
import com.devteria.identityservice.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.Optional;

@SpringBootTest
@Slf4j
public class AuthenticationServiceTest {

    @Autowired
    AuthenticationService authenticationService;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private PasswordEncoder passwordEncoder;



    private User user;
    private AuthenticationRequest request;
    private AuthenticationResponse response;

    @BeforeEach
    void init() {
        request = AuthenticationRequest.builder()
                .username("JohnDoe")
                .password("1234567890")
                .build();
        response = AuthenticationResponse.builder()
                .authenticated(true)
                .token("mocked-jwt-token")
                .build();
        String rawPassword = "1234567890";
        // Giả lập hành vi encode trả về một mật khẩu mã hóa giả
        String encodedPassword = "$2a$10$kBMoj0/fY1f/RjM5rlblDeHeWK7JGEWDoLMaxfJWnQJ/Ic3nTPCP6";
        Mockito.when(passwordEncoder.encode(rawPassword)).thenReturn(encodedPassword);
        user = User.builder()
                .id("59d93295-ff8d-4ac2-9d42-07642c8b7dca")
                .username("JohnDoe")
                .avatar("https://example.com/avatar.jpg")
                .firstName("John")
                .lastName("Doe")
                .dob(LocalDate.of(1990, 1, 1))
                .password(encodedPassword)
                .build();
    }
    @Test
    void authentication_validRequest() {
        // GIVEN
        Mockito.when(userRepository.findByUsername("JohnDoe")).thenReturn(Optional.of(user));
        Mockito.when(passwordEncoder.matches(request.getPassword(), user.getPassword())).thenReturn(true);
        log.info("request: "+request.getPassword());
        log.info("user: "+user.getPassword());
        // WHEN
        var actualResponse = authenticationService.authenticate(request);

        // THEN
        Assertions.assertThat(actualResponse.isAuthenticated()).isTrue();
        Assertions.assertThat(actualResponse.getToken()).isEqualTo("mocked-jwt-token");
    }
}
