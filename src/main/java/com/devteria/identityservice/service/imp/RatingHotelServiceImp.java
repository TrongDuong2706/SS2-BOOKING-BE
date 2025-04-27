package com.devteria.identityservice.service.imp;

import com.devteria.identityservice.dto.request.RatingHotelRequest;
import com.devteria.identityservice.dto.response.PaginatedRatingResponse;
import com.devteria.identityservice.dto.response.RatingHotelResponse;

import java.util.List;

public interface RatingHotelServiceImp {
    RatingHotelResponse ratingHotel(RatingHotelRequest request);
    PaginatedRatingResponse<RatingHotelResponse> getRatingHotelByHotelId(int hotelId, int page, int size);
}
