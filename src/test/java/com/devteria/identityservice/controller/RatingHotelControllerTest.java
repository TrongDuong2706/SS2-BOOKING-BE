package com.devteria.identityservice.controller;

import com.devteria.identityservice.dto.request.RatingHotelRequest;
import com.devteria.identityservice.dto.response.RatingHotelResponse;
import com.devteria.identityservice.service.imp.RatingHotelServiceImp;
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
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@Slf4j
@AutoConfigureMockMvc
public class RatingHotelControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RatingHotelServiceImp ratingHotelServiceImp;

    private RatingHotelRequest request;
    private RatingHotelResponse response;

    //Dùng BeforeEach để cho hàm init chạy trước @Test
    @BeforeEach
    void initData() {
        request = RatingHotelRequest.builder()
                .hotelId(18)
                .userId("59d93295-ff8d-4ac2-9d42-07642c8b7dca")
                .content("What a hotel, i very like this hotel, near the beach!")
                .point(5)
                .build();
        response = RatingHotelResponse.builder()
                .hotelId(18)
                .userId("59d93295-ff8d-4ac2-9d42-07642c8b7dca")
                .content("What a hotel, i very like this hotel, near the beach!")
                .point(5)
                .build();
    }

    @Test
    @WithMockUser
    void createRating_validRequest() throws Exception {
        //GIVEN
        //Vì content yêu cầu một String, mà Init của ta lại là Object nên ta cần sử dụng ObjectMapper để chuyển thành
        //String
        ObjectMapper objectMapper = new ObjectMapper();
        String content = objectMapper.writeValueAsString(request);

        Mockito.when(ratingHotelServiceImp.ratingHotel(ArgumentMatchers.any())).thenReturn(response);

        //WHEN
        //perform để tạo request
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/ratings")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(content))
                //THEN expect những cái gì
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("code").value(1000))
                .andExpect(MockMvcResultMatchers.jsonPath("result.hotelId").value(18))
                .andExpect(MockMvcResultMatchers.jsonPath("$.result.userId").value("59d93295-ff8d-4ac2-9d42-07642c8b7dca"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.result.content").value("What a hotel, i very like this hotel, near the beach!"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.result.point").value(5));
        ;

    }
}
