package com.devteria.identityservice.mapper;

import com.devteria.identityservice.dto.request.HotelCreationRequest;
import com.devteria.identityservice.dto.request.UserUpdateRequest;
import com.devteria.identityservice.dto.response.HotelResponse;
import com.devteria.identityservice.dto.response.ImageResponse;
import com.devteria.identityservice.entity.Hotel;
import com.devteria.identityservice.entity.Image;
import com.devteria.identityservice.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;
import java.util.Set;

@Mapper(componentModel = "spring")
public interface HotelMapper {

    @Mapping(source ="categoryId", target="hotelCategory.id")
    Hotel toHotel(HotelCreationRequest request);

    @Mapping(source ="hotel.id", target="hotelId")
    @Mapping(source ="hotelCategory.id", target="categoryId")
    HotelResponse toHotelResponse(Hotel hotel);
    void updateHotel(@MappingTarget Hotel hotel, HotelCreationRequest request);



}
