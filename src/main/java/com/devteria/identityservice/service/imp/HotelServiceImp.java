package com.devteria.identityservice.service.imp;

import com.devteria.identityservice.dto.request.HotelCreationRequest;
import com.devteria.identityservice.dto.response.HotelResponse;
import com.devteria.identityservice.dto.response.PaginatedResponse;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.List;

public interface HotelServiceImp {
     HotelResponse createHotel(HotelCreationRequest request, List<MultipartFile> files);
     PaginatedResponse<HotelResponse> getAllHotels(int page, int size,String city, String name, BigDecimal minPrice, BigDecimal maxPrice, Integer categoryId);
     void deleteHotel(int hotelId);
     HotelResponse updateHotel(HotelCreationRequest request, List<MultipartFile> files,int hotelId );
     HotelResponse getOneHotel(int hotelId);
     List<HotelResponse> getHotelByCategory(int categoryId);

     long getTotalHotels();
     PaginatedResponse<HotelResponse> getHotel(int page, int size);

}
