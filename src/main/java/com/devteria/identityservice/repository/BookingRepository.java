package com.devteria.identityservice.repository;

import com.devteria.identityservice.dto.response.BookingResponse;
import com.devteria.identityservice.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Integer> {
    @Query(value ="SELECT COUNT(*) FROM bookings", nativeQuery = true )
    long countTotalBooking();
    //bỏ b:
//    @Query(value = "SELECT DATE_FORMAT(created_at, '%Y-%m') AS month, " +
//            "SUM(total_price) AS total_revenue " +
//            "FROM bookings " +
//            "GROUP BY DATE_FORMAT(created_at, '%Y-%m') " +
//            "ORDER BY month", nativeQuery = true)
//    List<Object[]> findMonthlyRevenue();
    @Query(value = "SELECT DATE_FORMAT(b.created_at, '%Y-%m') AS month, " +
            "SUM(b.total_price) AS total_revenue " +
            "FROM bookings b " +
            "GROUP BY DATE_FORMAT(b.created_at, '%Y-%m') " +
            "ORDER BY month ASC", nativeQuery = true)
    List<Object[]> findMonthlyRevenue();
}
