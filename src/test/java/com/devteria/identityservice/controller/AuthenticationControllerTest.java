package com.devteria.identityservice.controller;

import com.devteria.identityservice.dto.request.AuthenticationRequest;
import com.devteria.identityservice.dto.response.AuthenticationResponse;
import com.devteria.identityservice.service.AuthenticationService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@Slf4j
@AutoConfigureMockMvc
public class AuthenticationControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthenticationService authenticationService;

    private AuthenticationRequest request;
    private AuthenticationResponse response;

    @BeforeEach
    void init(){
        request = AuthenticationRequest.builder()
                .username("TrongDuong")
                .password("12345678")
                .build();
        response = AuthenticationResponse.builder()
                .authenticated(true)
                .token("mocked-jwt-token")
                .build();
    }

    @Test
    void authentication_validRequest() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        String content = objectMapper.writeValueAsString(request);
        Mockito.when(authenticationService.authenticate(ArgumentMatchers.any())).thenReturn(response);

        mockMvc.perform(MockMvcRequestBuilders
                .post("/auth/token")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(content))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("code").value(1000))
                .andExpect(MockMvcResultMatchers.jsonPath("result.token").value("mocked-jwt-token"))
                .andExpect(MockMvcResultMatchers.jsonPath("result.authenticated").value(true));
    }
}
