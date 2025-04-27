package com.devteria.identityservice.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity(name ="hotels")
public class Hotel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "name", length = 100, nullable = false)
    String name;

    @ManyToOne
    @JoinColumn(name = "category_id")
    HotelCategory hotelCategory;

    @Column(name = "address", length = 255)
    String address;

    @Column(name = "city", length = 100)
    String city;

    @Column(name = "country", length = 100)
    String country;

    @Column(name = "phone", length = 20)
    String phone;

    @Column(name = "email", length = 100)
    String email;


    @Column(name = "description", columnDefinition = "TEXT")
    String description;

    @Column(name = "free_wifi")
    boolean freeWifi;

    @Column(name = "breakfast_included")
    boolean breakfastIncluded;

    @Column(name = "created_at", insertable = false, updatable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    Timestamp createAt;

    @Column(name = "price")
    BigDecimal price;

    @OneToMany(mappedBy = "hotel")
    Set<Room> rooms;

    @OneToMany(mappedBy = "hotel")
    Set<Booking> booking;

    @OneToMany(mappedBy = "hotel")
    Set<RatingHotel> ratingHotel;

    @OneToMany(mappedBy = "hotel", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Image> images;
}
