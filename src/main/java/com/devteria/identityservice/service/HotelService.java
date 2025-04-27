package com.devteria.identityservice.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.devteria.identityservice.dto.request.HotelCreationRequest;
import com.devteria.identityservice.dto.response.HotelResponse;
import com.devteria.identityservice.dto.response.PaginatedResponse;
import com.devteria.identityservice.entity.Hotel;
import com.devteria.identityservice.entity.Image;
import com.devteria.identityservice.entity.RatingHotel;
import com.devteria.identityservice.exception.AppException;
import com.devteria.identityservice.exception.ErrorCode;
import com.devteria.identityservice.mapper.HotelMapper;
import com.devteria.identityservice.repository.HotelRepository;
import com.devteria.identityservice.repository.ImageRepository;
import com.devteria.identityservice.repository.RatingHotelRepository;
import com.devteria.identityservice.service.imp.CloudinaryServiceImp;
import com.devteria.identityservice.service.imp.HotelServiceImp;
import jakarta.persistence.EntityNotFoundException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class HotelService implements HotelServiceImp {

    HotelMapper hotelMapper;
    HotelRepository hotelRepository;
    Cloudinary cloudinary;
    ImageRepository imageRepository;
    CloudinaryServiceImp cloudinaryServiceImp;
    RatingHotelRepository ratingHotelRepository;


    //Tạo Một khách sạn
    @Override
    public HotelResponse createHotel(HotelCreationRequest request, List<MultipartFile> files) {
        // Chuyển đổi yêu cầu thành thực thể khách sạn
        Hotel hotel = hotelMapper.toHotel(request);
        // Lưu thực thể khách sạn
        hotel = hotelRepository.save(hotel);
        // Tạo một tập hợp để lưu các đối tượng hình ảnh
        Set<Image> images = new HashSet<>();
        for (MultipartFile file : files) {
            try {
                // Tải hình ảnh lên Cloudinary
                Map<String, Object> uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());
                String url = uploadResult.get("url").toString();

                // Tạo và thiết lập đối tượng hình ảnh
                Image image = Image.builder()
                        .url(url)
                        .hotel(hotel)
                        .build();
                // Thêm hình ảnh vào tập hợp
                images.add(image);
            } catch (IOException e) {
                log.error("Error uploading image to Cloudinary", e);
            }
        }
        // Lưu tất cả các đối tượng hình ảnh và thiết lập chúng cho khách sạn
        hotel.setImages(images);
        imageRepository.saveAll(images);
        // Chuyển đổi thực thể khách sạn thành phản hồi
        HotelResponse hotelResponse = hotelMapper.toHotelResponse(hotel);
        // Đặt danh sách các hình ảnh vào phản hồi
        return hotelResponse;
    }

    //Lấy toàn bộ khách sạn
    @Override
    public PaginatedResponse<HotelResponse> getAllHotels(int page, int size,String city ,String name, BigDecimal minPrice, BigDecimal maxPrice, Integer categoryId) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<Hotel> hotelPage = hotelRepository.findByNameAndPriceAndCategory(city,name, minPrice, maxPrice, categoryId, pageRequest);

        List<Hotel> hotels = hotelPage.getContent();
        Map<Integer, List<RatingHotel>> hotelRatingsMap = hotels.stream()
                .collect(Collectors.toMap(
                        Hotel::getId,
                        hotel -> ratingHotelRepository.findByHotelId(hotel.getId())
                ));

        List<HotelResponse> hotelResponses = hotels.stream()
                .map(hotel -> {
                    List<RatingHotel> ratings = hotelRatingsMap.getOrDefault(hotel.getId(), List.of());
                    double totalPoints = 0.0;
                    int numberOfRatings = ratings.size();

                    for (RatingHotel rating : ratings) {
                        totalPoints += rating.getPoint();
                    }
                    double averageRating = (numberOfRatings > 0) ? totalPoints / numberOfRatings : 0.0;

                    HotelResponse response = hotelMapper.toHotelResponse(hotel);
                    response.setAverageRating(averageRating);
                    return response;
                })
                .toList();

        return PaginatedResponse.<HotelResponse>builder()
                .totalItems((int) hotelPage.getTotalElements())
                .totalPages(hotelPage.getTotalPages())
                .currentPage(hotelPage.getNumber())
                .pageSize(hotelPage.getSize())
                .hasNextPage(hotelPage.hasNext())
                .hasPreviousPage(hotelPage.hasPrevious())
                .hotels(hotelResponses)
                .build();
    }



    @Override
    public void deleteHotel(int hotelId) {
        Hotel hotel = hotelRepository.findById(hotelId).orElseThrow(() -> new EntityNotFoundException("Hotel not found with id " + hotelId));

        // Xóa ảnh từ Cloudinary
        Set<Image> images = hotel.getImages();
        for (Image image : images) {
            cloudinaryServiceImp.deleteImage(image.getUrl());
        }

        // Xóa khách sạn từ cơ sở dữ liệu
        hotelRepository.deleteById(hotelId);
    }

    //Hàm cập nhập Hotel
    @Override
    public HotelResponse updateHotel(HotelCreationRequest request, List<MultipartFile> files, int hotelId) {
        // Tìm khách sạn theo ID hoặc ném ngoại lệ nếu không tồn tại
        Hotel hotel = hotelRepository.findById(hotelId)
                .orElseThrow(() -> new AppException(ErrorCode.HOTEL_NOT_EXISTED));

        // Cập nhật thông tin khách sạn từ request
        hotelMapper.updateHotel(hotel, request);

        // Kiểm tra nếu có ảnh mới được tải lên
        if (files != null && !files.isEmpty()) {
            // Tạo một tập hợp để lưu các đối tượng hình ảnh mới
            Set<Image> newImages = new HashSet<>();
            for (MultipartFile file : files) {
                try {
                    // Tải hình ảnh lên Cloudinary
                    Map<String, Object> uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());
                    String url = uploadResult.get("url").toString();
                    // Tạo và thiết lập đối tượng hình ảnh
                    Image image = Image.builder()
                            .url(url)
                            .hotel(hotel)
                            .build();

                    // Thêm hình ảnh vào tập hợp mới
                    newImages.add(image);
                } catch (IOException e) {
                    log.error("Error uploading image to Cloudinary", e);
                }
            }

            // Xóa ảnh cũ trên Cloudinary và xóa trong cơ sở dữ liệu
            for (Image oldImage : hotel.getImages()) {
                try {
                    String publicId = oldImage.getUrl().substring(oldImage.getUrl().lastIndexOf("/") + 1, oldImage.getUrl().lastIndexOf("."));
                    cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
                } catch (IOException e) {
                    log.error("Error deleting image from Cloudinary", e);
                }
            }

            // Xóa tất cả các ảnh cũ khỏi cơ sở dữ liệu
            hotel.getImages().clear();
            hotel.getImages().addAll(newImages);
        }

        // Lưu thực thể khách sạn đã được cập nhật
        hotel = hotelRepository.save(hotel);

        // Chuyển đổi thực thể khách sạn thành phản hồi
        HotelResponse hotelResponse = hotelMapper.toHotelResponse(hotel);

        return hotelResponse;
    }
    @Override
    public HotelResponse getOneHotel(int hotelId) {
        // Lấy thông tin khách sạn từ cơ sở dữ liệu
        Hotel hotel = hotelRepository.findById(hotelId)
                .orElseThrow(() -> new AppException(ErrorCode.HOTEL_NOT_EXISTED));

        // Lấy tất cả các đánh giá của khách sạn
        List<RatingHotel> ratings = ratingHotelRepository.findByHotelId(hotelId);

        // Tính tổng điểm và số lượng đánh giá
        double totalPoints = 0.0;
        int numberOfRatings = ratings.size();

        for (RatingHotel rating : ratings) {
            totalPoints += rating.getPoint();
        }

        // Tính điểm đánh giá trung bình
        double averageRating = (numberOfRatings > 0) ? totalPoints / numberOfRatings : 0.0;

        // Chuyển đổi khách sạn thành HotelResponse và thiết lập điểm đánh giá trung bình
        HotelResponse response = hotelMapper.toHotelResponse(hotel);
        response.setAverageRating(averageRating);

        return response;
    }

    @Override
    public List<HotelResponse> getHotelByCategory(int categoryId) {
        List<Hotel> hotels = hotelRepository.findByHotelCategoryId(categoryId);
        return hotels.stream().map(hotelMapper::toHotelResponse).toList();
    }

    //Hàm đếm có tổng bao nhiêu Hotel
    @Override
    public long getTotalHotels() {
        return hotelRepository.countTotalHotels();
    }

    @Override
    public PaginatedResponse<HotelResponse> getHotel(int page, int size) {
        PageRequest pageRequest = PageRequest.of(page,size);
        Page<Hotel> hotel = hotelRepository.findAll(pageRequest);
        List<HotelResponse> hotelResponses = hotel.getContent().stream().map(hotelMapper::toHotelResponse).toList();
        return PaginatedResponse.<HotelResponse>builder()
                .totalItems((int) hotel.getTotalElements())
                .totalPages(hotel.getTotalPages())
                .currentPage(hotel.getNumber())
                .pageSize(hotel.getSize())
                .hasNextPage(hotel.hasNext())
                .hasPreviousPage(hotel.hasPrevious())
                .hotels(hotelResponses)
                .build();
    }

}
