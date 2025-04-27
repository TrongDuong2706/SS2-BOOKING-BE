package com.devteria.identityservice.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RatingHotelResponse {
    int hotelId;
    String userId;
    String content;
    double point;
    String userName;  // Thêm trường tên người dùng
    String userAvatar;
    LocalDateTime createdAt; // Thêm thuộc tính thời gian


}
