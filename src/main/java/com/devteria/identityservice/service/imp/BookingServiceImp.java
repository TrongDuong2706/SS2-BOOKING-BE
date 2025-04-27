package com.devteria.identityservice.service.imp;

import com.devteria.identityservice.dto.request.BookingRequest;
import com.devteria.identityservice.dto.response.BookingResponse;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

public interface BookingServiceImp {

    BookingResponse createBooking(BookingRequest request, HttpServletRequest httpRequest);
    BookingResponse getOneBooking(int bookingId);
    Long getTotalBooking();
    List<Object[]> getMonthlyRevenue();
}
