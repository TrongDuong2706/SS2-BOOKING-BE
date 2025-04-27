package com.devteria.identityservice.controller;

import com.devteria.identityservice.dto.request.ApiResponse;
import com.devteria.identityservice.dto.request.HotelCreationRequest;
import com.devteria.identityservice.dto.response.HotelResponse;
import com.devteria.identityservice.dto.response.PaginatedResponse;
import com.devteria.identityservice.entity.Hotel;
import com.devteria.identityservice.service.imp.HotelServiceImp;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/hotels")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class HotelController {

    HotelServiceImp hotelServiceImp;

    @PostMapping()
    ApiResponse<HotelResponse> createHotel(@RequestPart("hotel")HotelCreationRequest request,
                                           @RequestPart("images") List<MultipartFile> files){
        var hotel = hotelServiceImp.createHotel(request, files);
        return ApiResponse.<HotelResponse>builder()
                .result(hotel)
                .build();
    }
    //Lấy toàn bộ danh sách Hotel
    @GetMapping()
    public ApiResponse<PaginatedResponse<HotelResponse>> getAllHotels(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "4") int size,
            @RequestParam(required = false) String city,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice,
            @RequestParam(required = false) Integer categoryId) {

        int adjustedPage = Math.max(page - 1, 0);
        var hotels = hotelServiceImp.getAllHotels(adjustedPage, size,city, name, minPrice, maxPrice, categoryId);
        return ApiResponse.<PaginatedResponse<HotelResponse>>builder()
                .result(hotels)
                .build();
    }
    @DeleteMapping("/{hotelId}")
    public ApiResponse<String> deleteHotel(@PathVariable int hotelId){
        hotelServiceImp.deleteHotel(hotelId);
        return ApiResponse.<String>builder()
                .result("Hotel has been delete")
                .build();
    }

    @PutMapping("/{hotelId}")
    public ApiResponse<HotelResponse> updateHotel(
            @PathVariable int hotelId,
            @RequestPart("hotel") HotelCreationRequest request,
            @RequestPart(value ="images", required = false) List<MultipartFile> files){
    HotelResponse updatedHotel = hotelServiceImp.updateHotel(request, files, hotelId);
    return ApiResponse.<HotelResponse>builder()
            .result(updatedHotel)
            .build();
    }

    @GetMapping("/{hotelId}")
    public ApiResponse<HotelResponse> getOneHotel(@PathVariable int hotelId){
        return ApiResponse.<HotelResponse>builder()
                .result(hotelServiceImp.getOneHotel(hotelId))
                .build();
    }
    @GetMapping("/category/{categoryId}")
    public ApiResponse<List<HotelResponse>> getHotelByCateogry(@PathVariable int categoryId){
        return ApiResponse.<List<HotelResponse>>builder()
                .result(hotelServiceImp.getHotelByCategory(categoryId))
                .build();
    }
    @GetMapping("totalHotels")
    public ApiResponse<Long> getTotalHotels(){
        return ApiResponse.<Long>builder()
                .result(hotelServiceImp.getTotalHotels())
                .build();
    }
    @GetMapping("/hotel_home")
    public ApiResponse<PaginatedResponse<HotelResponse>> getHotel(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size){
        int adjustedPage = Math.max(page - 1, 0);
        var hotel = hotelServiceImp.getHotel(adjustedPage,size);
        return ApiResponse.<PaginatedResponse<HotelResponse>>builder()
                .result(hotel)
                .build();
    }
}
