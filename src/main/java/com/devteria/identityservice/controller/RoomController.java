package com.devteria.identityservice.controller;

import com.devteria.identityservice.dto.request.ApiResponse;
import com.devteria.identityservice.dto.request.HotelCreationRequest;
import com.devteria.identityservice.dto.request.RoomCreationRequest;
import com.devteria.identityservice.dto.response.HotelResponse;
import com.devteria.identityservice.dto.response.PaginatedResponse;
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

    @GetMapping()
    public ApiResponse<PaginatedResponse<RoomResponse>> getALlRoom( @RequestParam(defaultValue = "1") int page,
                                                 @RequestParam(defaultValue = "4") int size){
        int adjustedPage = Math.max(page - 1, 0);
        var rooms = roomServiceImp.getAllRooms(adjustedPage, size);
        return ApiResponse.<PaginatedResponse<RoomResponse>>builder()
                .result(rooms)
                .build();
    }
    @PutMapping("/{roomId}")
    public ApiResponse<RoomResponse> updateRoom(@PathVariable int roomId,
                                                @RequestPart("room") RoomCreationRequest request,
                                                @RequestPart(value ="room_images", required = false) List<MultipartFile> files){
        RoomResponse updateRoom = roomServiceImp.updateRoom(request, files,roomId );
        return ApiResponse.<RoomResponse>builder()
                .result(updateRoom)
                .build();
    }
    @DeleteMapping("/{roomId}")
    public ApiResponse<Void> deleteRoom(@PathVariable int roomId) {
        roomServiceImp.deleteRoom(roomId);
        return ApiResponse.<Void>builder()
                .message("Room deleted successfully")
                .build();
    }

}
