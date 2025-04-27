package com.devteria.identityservice.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RoomCreationRequest {
    int bedCount;
    String description;
    boolean isAvailable;
    BigDecimal price;
    BigDecimal roomArea;
    String roomNumber;
    String roomType;
    String status;
    int hotelId;

}
