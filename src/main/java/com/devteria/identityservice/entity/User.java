package com.devteria.identityservice.entity;

import java.time.LocalDate;
import java.util.Set;

import jakarta.persistence.*;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;
    String username;
    @Column(name ="avatar")
    String avatar;
    String password;
    String firstName;
    LocalDate dob;
    String lastName;

    @ManyToMany
    Set<Role> roles;
    @OneToMany(mappedBy = "user")
    Set<Booking> bookings;

    @OneToMany(mappedBy = "user")
    Set<RatingHotel> ratingHotel;
}
