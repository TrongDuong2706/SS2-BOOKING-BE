package com.devteria.identityservice.service;

import com.devteria.identityservice.dto.request.RatingHotelRequest;
import com.devteria.identityservice.dto.response.RatingHotelResponse;
import com.devteria.identityservice.entity.Hotel;
import com.devteria.identityservice.entity.RatingHotel;
import com.devteria.identityservice.entity.User;
import com.devteria.identityservice.repository.RatingHotelRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@SpringBootTest
public class RatingHotelServiceTest {
    @Autowired
    RatingHotelService ratingHotelService;

    @MockBean
    private RatingHotelRepository ratingHotelRepository;
    private RatingHotelRequest request;
    private RatingHotelResponse response;
    private RatingHotel ratingHotel;
    private Hotel hotel;
    private User user;

    //Dùng BeforeEach để cho hàm init chạy trước @Test
    @BeforeEach
    void initData(){
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
        hotel = Hotel.builder()
                .id(18) // ID của hotel để khớp với request
                .name("Beach Hotel")
                .address("123 Beach St.")
                .city("Miami")
                .country("USA")
                .phone("+123456789")
                .email("contact@beachhotel.com")
                .description("A lovely hotel near the beach with free Wi-Fi and breakfast included.")
                .freeWifi(true)
                .breakfastIncluded(true)
                .price(new BigDecimal("200.00"))
                .build();
        user = User.builder()
                .id("59d93295-ff8d-4ac2-9d42-07642c8b7dca") // ID của user để khớp với request
                .username("JohnDoe")
                .avatar("https://example.com/avatar.jpg")
                .firstName("John")
                .lastName("Doe")
                .dob(LocalDate.of(1990, 1, 1))
                .password("password123")
                .build();
        ratingHotel = RatingHotel.builder()
                .hotel(hotel)
                .user(user)
                .content("Nice Hotel")
                .point(5)
                .createdAt(LocalDateTime.now())
                .build();
    }

    @Test
    void createRating_validRequest(){
        //GIVEN
        Mockito.when(ratingHotelRepository.save(ArgumentMatchers.any())).thenReturn(ratingHotel);

        //WHEN
        var response = ratingHotelService.ratingHotel(request);

        //THEN
        Assertions.assertThat(response.getHotelId()).isEqualTo(18);
        Assertions.assertThat(response.getContent()).isEqualTo("What a hotel, i very like this hotel, near the beach!");
    }
}
