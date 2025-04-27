package com.devteria.identityservice.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.devteria.identityservice.dto.request.RoomCreationRequest;
import com.devteria.identityservice.dto.response.RoomResponse;
import com.devteria.identityservice.entity.Image;
import com.devteria.identityservice.entity.Room;
import com.devteria.identityservice.entity.RoomImage;
import com.devteria.identityservice.exception.AppException;
import com.devteria.identityservice.exception.ErrorCode;
import com.devteria.identityservice.mapper.RoomMapper;
import com.devteria.identityservice.repository.RoomImageRepository;
import com.devteria.identityservice.repository.RoomRepository;
import com.devteria.identityservice.repository.UserRepository;
import com.devteria.identityservice.service.imp.CloudinaryServiceImp;
import com.devteria.identityservice.service.imp.RoomServiceImp;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RoomService implements RoomServiceImp {
    RoomRepository roomRepository;
    RoomMapper roomMapper;

    RoomImageRepository roomImageRepository;
    CloudinaryServiceImp cloudinaryServiceImp;
    Cloudinary cloudinary;
    UserRepository userRepository;

    //Phần Redis
    private final BaseRedisService<String, String, RoomResponse> redisService;
    private static final String ROOM_CACHE_PREFIX = "room:";
    public RoomResponse createRoom(RoomCreationRequest request, List<MultipartFile> files){
        Room room = roomMapper.toRoom(request);
        room = roomRepository.save(room);
        Set<RoomImage> images = new HashSet<>();

        for (MultipartFile file : files) {
            try {
                // Tải hình ảnh lên Cloudinary
                Map<String, Object> uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());
                String url = uploadResult.get("url").toString();

                // Tạo và thiết lập đối tượng hình ảnh
                RoomImage image = RoomImage.builder()
                        .url(url)
                        .room(room)
                        .build();

                // Thêm hình ảnh vào tập hợp
                images.add(image);
            } catch (IOException e) {
                log.error("Error uploading image to Cloudinary", e);
            }
        }
        room.setRoomImages(images);
        roomImageRepository.saveAll(images);
        RoomResponse roomResponse = roomMapper.toRoomResponse(room);
        return roomResponse;
    }
    @Override
    public List<RoomResponse> getRoomsByHotelId(int hotelId){
        List<Room> rooms = roomRepository.findByHotelId(hotelId);
        return rooms.stream()
                .map(roomMapper::toRoomResponse).toList();
    }
    @Override
    public RoomResponse getOneRoom(int roomId) {
        // Tạo key cho Redis
        String redisKey = ROOM_CACHE_PREFIX + roomId;
        // Kiểm tra xem dữ liệu đã có trong Redis chưa
        RoomResponse cachedRoom = redisService.get(redisKey);
        if (cachedRoom != null) {
            log.info("Found room in Redis: {}", cachedRoom);
            return cachedRoom;
        }
        log.info("Room not found in Redis, querying database...");
        var roomEntity = roomRepository.findById(roomId).orElseThrow(
                () -> new AppException(ErrorCode.ROOM_NOT_EXISTED));
        // Log thông tin roomEntity để kiểm tra
        log.info("Found room: {}", roomEntity);
        RoomResponse roomResponse = roomMapper.toRoomResponse(roomEntity);
        redisService.set( redisKey, roomResponse);
        return roomResponse;
    }


}
