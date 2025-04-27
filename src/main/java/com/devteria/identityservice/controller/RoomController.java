package com.devteria.identityservice.controller;

import com.devteria.identityservice.dto.request.ApiResponse;
import com.devteria.identityservice.dto.request.HotelCreationRequest;
import com.devteria.identityservice.dto.request.RoomCreationRequest;
import com.devteria.identityservice.dto.response.RoomResponse;
import com.devteria.identityservice.service.RoomService;
import com.devteria.identityservice.service.imp.RoomServiceImp;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/rooms")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class RoomController {
    RoomServiceImp roomServiceImp;
    RoomService roomService;

    @PostMapping()
    public ApiResponse<RoomResponse> createRoom(
            @RequestPart("room") RoomCreationRequest request,
            @RequestPart("room_images") List<MultipartFile> files){

        var room = roomServiceImp.createRoom(request,files);
        return ApiResponse.<RoomResponse>builder()
                .result(room)
                .build();
    }

    @GetMapping("/{hotelId}")
    public ApiResponse<List<RoomResponse>> getRoomByHotelId(@PathVariable int hotelId){
        var room = roomServiceImp.getRoomsByHotelId(hotelId);
        return ApiResponse.<List<RoomResponse>>builder()
                .result(room)
                .build();
    }

    @GetMapping("/payment/{roomId}")
    public ApiResponse<RoomResponse> getOneRoom(@PathVariable int roomId){
        var getOneRoom = roomService.getOneRoom(roomId);
        return ApiResponse.<RoomResponse>builder()
                .result(getOneRoom)
                .build();
    }
    @GetMapping("payment")
    public String test(){
        return "hello";
    }

}
