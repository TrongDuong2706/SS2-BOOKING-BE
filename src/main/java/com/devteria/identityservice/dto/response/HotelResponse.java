package com.devteria.identityservice.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class HotelResponse {
    int hotelId;
    String name;
    String address;
    String city;
    String country;
    String phone;
    String email;
    String description;
    BigDecimal price;
    int categoryId;
    boolean freeWifi;
    boolean breakfastIncluded;
    List<ImageResponse> images;
    double averageRating; // Thêm trường điểm đánh giá trung bình

}
