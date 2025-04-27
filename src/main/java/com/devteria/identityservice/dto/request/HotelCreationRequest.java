package com.devteria.identityservice.dto.request;

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
public class HotelCreationRequest {
    String name;
    String address;
    String city;
    String country;
    String phone;
    String email;
    String description;
    int categoryId;
    BigDecimal price;
    boolean freeWifi;
    boolean breakfastIncluded;
}
