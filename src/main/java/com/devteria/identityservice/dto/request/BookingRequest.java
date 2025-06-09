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
public class BookingRequest {
    Timestamp checkInDate;
    Timestamp checkOutDate;
    BigDecimal totalPrice;
    int roomId;
    String userId;
    int hotelId;
    String paymentMethod;
    String specialRequest;
    String email;
    String firstName;
    String lastName;
    String phoneNumber;
    int numberOfRoom;
    BigDecimal amount; // Số tiền cần thanh toán
    String bankCode;   // Mã ngân hàng (nếu cần)
}
