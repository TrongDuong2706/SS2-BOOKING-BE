package com.devteria.identityservice.repository;

import com.devteria.identityservice.entity.RatingHotel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RatingHotelRepository extends JpaRepository<RatingHotel, Integer> {
    Page<RatingHotel> findByHotelId(int hotelId, Pageable pageable);
    List<RatingHotel> findByHotelId(int hotelId);

}
