package com.devteria.identityservice.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity(name ="images")
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name ="url")
    private String url;

    @ManyToOne
    @JoinColumn(name = "hotel_id")
    private Hotel hotel;
}
