package com.devteria.identityservice.mapper;


import com.devteria.identityservice.dto.request.HotelCreationRequest;
import com.devteria.identityservice.dto.request.RoomCreationRequest;
import com.devteria.identityservice.dto.response.ImageResponse;
import com.devteria.identityservice.dto.response.RoomResponse;
import com.devteria.identityservice.entity.Hotel;
import com.devteria.identityservice.entity.Room;
import com.devteria.identityservice.entity.RoomImage;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface RoomMapper {
    @Mapping(source = "hotelId", target = "hotel.id")
    @Mapping(source ="status", target="status")
    Room toRoom(RoomCreationRequest room);
    @Mapping(source = "roomImages", target = "images")
    @Mapping(source = "hotel.id", target = "hotelId")
    @Mapping(source ="status", target="status")
    RoomResponse toRoomResponse(Room room);

    void updateRoom(@MappingTarget Room room, RoomCreationRequest request);

}
