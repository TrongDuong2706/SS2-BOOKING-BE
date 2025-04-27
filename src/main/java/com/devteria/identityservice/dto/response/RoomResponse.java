package com.devteria.identityservice.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RoomResponse {
    int id;
    int bedCount;
    String description;
    boolean isAvailable;
    BigDecimal price;
    BigDecimal roomArea;
    String roomNumber;
    String roomType;
    String status;
    int hotelId;
    List<ImageResponse> images;

}
