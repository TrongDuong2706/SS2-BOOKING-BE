package com.devteria.identityservice.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity(name = "rooms")
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    int id;

    @ManyToOne
    @JoinColumn(name = "hotel_id")
    Hotel hotel;

    @Column(name = "room_number", length = 20, nullable = false)
    String roomNumber;

    @Column(name = "room_type", length = 50)
    String roomType;

    @Column(name = "price", precision = 10, scale = 2)
    BigDecimal price;

    @Column(name = "description", columnDefinition = "TEXT")
    String description;

    @Column(name = "status")
    String status;

    @Column(name = "bed_count")
    int bedCount;

    @Column(name = "room_area", precision = 5, scale = 2)
    BigDecimal roomArea;

    @Column(name = "is_available")
    boolean isAvailable;

    @Column(name = "created_at", insertable = false, updatable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    Timestamp createdAt;


    @OneToMany(mappedBy = "room")
    Set<Booking> bookings;

    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<RoomImage> roomImages;

}
