package com.devteria.identityservice.mapper;

import com.devteria.identityservice.dto.request.RatingHotelRequest;
import com.devteria.identityservice.dto.response.RatingHotelResponse;
import com.devteria.identityservice.entity.RatingHotel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RatingHotelMapper {

    @Mapping(source ="hotelId", target="hotel.id")
    @Mapping(source ="userId", target="user.id")
    RatingHotel toRatingHotel(RatingHotelRequest request);

    @Mapping(source ="hotel.id", target="hotelId")
    @Mapping(source ="user.id", target="userId")
    RatingHotelResponse toRatingHotelResponse(RatingHotel ratingHotel);

}
