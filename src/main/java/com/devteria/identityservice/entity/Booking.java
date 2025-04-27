package com.devteria.identityservice.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity(name ="bookings")
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    int id;

    @ManyToOne
    @JoinColumn(name = "hotel_id")
    Hotel hotel;

    @ManyToOne
    @JoinColumn(name = "room_id")
    Room room;

    @ManyToOne
    @JoinColumn(name = "user_id")
    User user;

    @Column(name = "check_in_date", nullable = false)
    Timestamp checkInDate;

    @Column(name = "check_out_date", nullable = false)
    Timestamp checkOutDate;

    @Column(name = "total_price", precision = 10, scale = 2)
    BigDecimal totalPrice;

    @Column(name = "booking_status", length = 50)
    String bookingStatus;

    @Column(name = "payment_status")
    String paymentStatus; // e.g., PAID, UNPAID

    @Column(name = "code")
    String code;

    @Column(name = "first_name")
    String firstName;
    @Column(name = "last_name")
    String lastName;

    @Column(name = "email")
    String email;

    @Column(name = "phone_number")
    String phoneNumber;

    @Column(name = "number_of_room")
    int numberOfRoom;

    @Column(name = "payment_method")
    String paymentMethod; // e.g., VNPAY

    @Column(name = "special_requests", columnDefinition = "TEXT")
    String specialRequests; // Yêu cầu đặc biệt của khách
    @Column(name = "created_at", insertable = false, updatable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    Timestamp createdAt;
}
