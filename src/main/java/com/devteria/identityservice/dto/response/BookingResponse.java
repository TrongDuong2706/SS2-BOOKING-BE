package com.devteria.identityservice.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BookingResponse {
    int id;
    Timestamp checkInDate;
    Timestamp checkOutDate;
    Timestamp createAt;
    BigDecimal totalPrice;
    int roomId;
    String userId;
    int hotelId;
    String paymentMethod;
    String specialRequest;
    String code;
    String email;
    String firstName;
    String lastName;
    String phoneNumber;
    int numberOfRoom;
    String paymentUrl;  // Added field
    String bankCode;


}
