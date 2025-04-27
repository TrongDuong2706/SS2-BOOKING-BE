package com.devteria.identityservice.service;

import com.devteria.identityservice.dto.request.RatingHotelRequest;
import com.devteria.identityservice.dto.response.PaginatedRatingResponse;
import com.devteria.identityservice.dto.response.RatingHotelResponse;
import com.devteria.identityservice.dto.response.RoomResponse;
import com.devteria.identityservice.entity.Hotel;
import com.devteria.identityservice.entity.RatingHotel;
import com.devteria.identityservice.entity.Room;
import com.devteria.identityservice.entity.User;
import com.devteria.identityservice.exception.AppException;
import com.devteria.identityservice.exception.ErrorCode;
import com.devteria.identityservice.mapper.RatingHotelMapper;
import com.devteria.identityservice.repository.RatingHotelRepository;
import com.devteria.identityservice.repository.UserRepository;
import com.devteria.identityservice.service.imp.RatingHotelServiceImp;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RatingHotelService implements RatingHotelServiceImp {

    RatingHotelRepository ratingHotelRepository;
    RatingHotelMapper ratingHotelMapper;

    UserRepository userRepository;

    @Override
    public RatingHotelResponse ratingHotel(RatingHotelRequest request) {
        RatingHotel ratingHotel = ratingHotelMapper.toRatingHotel(request);
        ratingHotelRepository.save(ratingHotel);
        return ratingHotelMapper.toRatingHotelResponse(ratingHotel);
    }

    @Override
    public PaginatedRatingResponse<RatingHotelResponse> getRatingHotelByHotelId(int hotelId, int page, int size) {
        // Create pageable object
        PageRequest pageRequest = PageRequest.of(page, size);

        // Get the paginated list of ratings by hotelId
        Page<RatingHotel> ratingPage = ratingHotelRepository.findByHotelId(hotelId, pageRequest);

        // Map the ratings to RatingHotelResponse
        List<RatingHotelResponse> ratingHotelResponses = ratingPage.stream()
                .map(rating -> {
                    // Fetch user details
                    User user = userRepository.findById(rating.getUser().getId())
                            .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

                    return RatingHotelResponse.builder()
                            .hotelId(hotelId)
                            .userId(rating.getUser().getId())
                            .content(rating.getContent())
                            .point(rating.getPoint())
                            .userName(user.getUsername())
                            .userAvatar(user.getAvatar())
                            .createdAt(rating.getCreatedAt())
                            .build();
                }).toList();

        // Create the PaginatedRatingResponse
        PaginatedRatingResponse<RatingHotelResponse> response = PaginatedRatingResponse.<RatingHotelResponse>builder()
                .totalItems((int) ratingPage.getTotalElements())
                .totalPages(ratingPage.getTotalPages())
                .currentPage(ratingPage.getNumber())
                .pageSize(ratingPage.getSize())
                .hasNextPage(ratingPage.hasNext())
                .hasPreviousPage(ratingPage.hasPrevious())
                .ratingHotel(ratingHotelResponses)
                .build();

        return response;
    }
}
