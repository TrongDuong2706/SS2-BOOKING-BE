package com.devteria.identityservice.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.devteria.identityservice.dto.request.RoomCreationRequest;
import com.devteria.identityservice.dto.response.PaginatedResponse;
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
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
//    @Override
//    public RoomResponse getOneRoom(int roomId) {
//        // Tạo key cho Redis
//        String redisKey = ROOM_CACHE_PREFIX + roomId;
//        // Kiểm tra xem dữ liệu đã có trong Redis chưa
//        RoomResponse cachedRoom = redisService.get(redisKey);
//        if (cachedRoom != null) {
//            log.info("Found room in Redis: {}", cachedRoom);
//            return cachedRoom;
//        }
//        log.info("Room not found in Redis, querying database...");
//        var roomEntity = roomRepository.findById(roomId).orElseThrow(
//                () -> new AppException(ErrorCode.ROOM_NOT_EXISTED));
//        // Log thông tin roomEntity để kiểm tra
//        log.info("Found room: {}", roomEntity);
//        RoomResponse roomResponse = roomMapper.toRoomResponse(roomEntity);
//        redisService.set( redisKey, roomResponse);
//        return roomResponse;
//    }
@Override
public RoomResponse getOneRoom(int roomId) {
    // Truy vấn trực tiếp từ database
    var roomEntity = roomRepository.findById(roomId)
            .orElseThrow(() -> new AppException(ErrorCode.ROOM_NOT_EXISTED));

    // Log thông tin roomEntity để kiểm tra
    log.info("Found room: {}", roomEntity);

    return roomMapper.toRoomResponse(roomEntity);
}

    @Override
    public PaginatedResponse<RoomResponse> getAllRooms(int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<Room> roomPage = roomRepository.findAll(pageRequest);

        List<RoomResponse> roomResponses = roomPage.getContent()
                .stream()
                .map(roomMapper::toRoomResponse)
                .toList();
        return PaginatedResponse.<RoomResponse>builder()
                .totalItems((int) roomPage.getTotalElements())
                .totalPages(roomPage.getTotalPages())
                .currentPage(roomPage.getNumber())
                .pageSize(roomPage.getSize())
                .hasNextPage(roomPage.hasNext())
                .hasPreviousPage(roomPage.hasPrevious())
                .hotels(roomResponses)
                .build();
    }

    @Override
    public RoomResponse updateRoom(RoomCreationRequest request, List<MultipartFile> files, int roomId) {
        // 1. Lấy room từ DB hoặc throw lỗi
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new AppException(ErrorCode.ROOM_NOT_EXISTED));

        // 2. Cập nhật dữ liệu room từ request
        roomMapper.updateRoom(room, request); // bạn cần đảm bảo có method updateRoom trong RoomMapper

        // 3. Nếu có ảnh mới được gửi lên
        if (files != null && !files.isEmpty()) {
            Set<RoomImage> newImages = new HashSet<>();

            for (MultipartFile file : files) {
                try {
                    Map<String, Object> uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());
                    String url = uploadResult.get("url").toString();

                    RoomImage image = RoomImage.builder()
                            .url(url)
                            .room(room)
                            .build();

                    newImages.add(image);
                } catch (IOException e) {
                    log.error("Error uploading image to Cloudinary", e);
                }
            }

            // 4. Xoá ảnh cũ khỏi Cloudinary (nếu muốn) + DB
            for (RoomImage oldImage : room.getRoomImages()) {
                try {
                    String publicId = oldImage.getUrl().substring(oldImage.getUrl().lastIndexOf("/") + 1, oldImage.getUrl().lastIndexOf("."));
                    cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
                } catch (IOException e) {
                    log.error("Error deleting room image from Cloudinary", e);
                }
            }

            // 5. Xoá ảnh cũ khỏi DB và gán ảnh mới
            room.getRoomImages().clear();
            room.getRoomImages().addAll(newImages);
        }

        // 6. Lưu lại room
        room = roomRepository.save(room);
        roomImageRepository.saveAll(room.getRoomImages());

        // 7. Trả về RoomResponse
        return roomMapper.toRoomResponse(room);
    }

    @Override
    @Transactional
    public void deleteRoom(int roomId) {
        // 1. Kiểm tra phòng có tồn tại không
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new AppException(ErrorCode.ROOM_NOT_EXISTED));

        // 2. Xoá ảnh trên Cloudinary nếu có
        Set<RoomImage> roomImages = room.getRoomImages();
        for (RoomImage image : roomImages) {
            try {
                String url = image.getUrl();
                String publicId = url.substring(url.lastIndexOf("/") + 1, url.lastIndexOf("."));
                cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
            } catch (IOException e) {
                log.error("Error deleting room image from Cloudinary", e);
            }
        }

        // 3. Xoá ảnh khỏi DB
        roomImageRepository.deleteAll(roomImages);

        // 4. Xoá phòng khỏi DB
        roomRepository.delete(room);

        // 5. Xoá khỏi Redis cache (nếu dùng)
        String redisKey = ROOM_CACHE_PREFIX + roomId;
        redisService.delete(redisKey);

        log.info("Deleted room with ID: {}", roomId);
    }



}
