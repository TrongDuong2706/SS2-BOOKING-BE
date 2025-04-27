package com.devteria.identityservice.controller;

import com.devteria.identityservice.dto.request.ApiResponse;
import com.devteria.identityservice.dto.request.RatingHotelRequest;
import com.devteria.identityservice.dto.response.PaginatedRatingResponse;
import com.devteria.identityservice.dto.response.RatingHotelResponse;
import com.devteria.identityservice.service.imp.RatingHotelServiceImp;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/ratings")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class RatingHotelController {
    RatingHotelServiceImp ratingHotelServiceImp;
    @PostMapping()
    public ApiResponse<RatingHotelResponse> createRatingHotel(@RequestBody RatingHotelRequest request){
        var ratingHotel = ratingHotelServiceImp.ratingHotel(request);
        return ApiResponse.<RatingHotelResponse>builder()
                .result(ratingHotel)
                .build();
    }

    @GetMapping("/{hotelId}")
    public ApiResponse<PaginatedRatingResponse<RatingHotelResponse>> getAllHotelById
            (@PathVariable int hotelId,
             @RequestParam(defaultValue = "1") int page,
             @RequestParam(defaultValue = "4") int size) {
        int adjustedPage = Math.max(page - 1, 0);
        var getRatingHotel = ratingHotelServiceImp.getRatingHotelByHotelId(hotelId, adjustedPage, size);
        return ApiResponse.<PaginatedRatingResponse<RatingHotelResponse>>builder()
                .result(getRatingHotel)
                .build();
    }
}
