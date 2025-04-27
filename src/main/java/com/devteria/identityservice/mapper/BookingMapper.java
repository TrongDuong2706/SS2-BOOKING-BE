package com.devteria.identityservice.mapper;

import com.devteria.identityservice.dto.request.BookingRequest;
import com.devteria.identityservice.dto.response.BookingResponse;
import com.devteria.identityservice.entity.Booking;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface BookingMapper {
    @Mapping(source ="roomId", target="room.id")
    @Mapping(source ="userId", target="user.id")
    @Mapping(source ="hotelId", target="hotel.id")
    @Mapping(source ="specialRequest", target="specialRequests")
    Booking toBooking(BookingRequest request);
    @Mapping(source ="room.id", target="roomId")
    @Mapping(source ="user.id", target="userId")
    @Mapping(source ="hotel.id", target="hotelId")
    @Mapping(source ="specialRequests", target="specialRequest")
    BookingResponse toBookingResponse(Booking booking);
}
