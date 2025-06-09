package com.devteria.identityservice.service.imp;


import com.devteria.identityservice.dto.request.HotelCreationRequest;
import com.devteria.identityservice.dto.request.RoomCreationRequest;
import com.devteria.identityservice.dto.response.HotelResponse;
import com.devteria.identityservice.dto.response.PaginatedResponse;
import com.devteria.identityservice.dto.response.RoomResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface RoomServiceImp {
    RoomResponse createRoom(RoomCreationRequest request, List<MultipartFile> files);
    List<RoomResponse> getRoomsByHotelId(int hotelId);
    RoomResponse getOneRoom(int roomId);
    PaginatedResponse<RoomResponse> getAllRooms(int page, int size);
    RoomResponse updateRoom(RoomCreationRequest request, List<MultipartFile> files, int roomId );
    void deleteRoom(int roomId);
}
