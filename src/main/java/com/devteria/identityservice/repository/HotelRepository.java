package com.devteria.identityservice.repository;

import com.devteria.identityservice.entity.Hotel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface HotelRepository extends JpaRepository<Hotel, Integer> {
    @Query(value = "SELECT * FROM hotels h WHERE " +
            "(:city IS NULL OR h.city = :city) AND " +
            "(:name IS NULL OR h.name LIKE %:name%) AND " +
            "(:minPrice IS NULL OR h.price >= :minPrice) AND " +
            "(:maxPrice IS NULL OR h.price <= :maxPrice) AND " +
            "(:categoryId IS NULL OR h.category_id = :categoryId)",
            nativeQuery = true)
    Page<Hotel> findByNameAndPriceAndCategory(@Param("city") String city,
                                              @Param("name") String name,
                                              @Param("minPrice") BigDecimal minPrice,
                                              @Param("maxPrice") BigDecimal maxPrice,
                                              @Param("categoryId") Integer categoryId,
                                              Pageable pageable);
    Page<Hotel> findAll(Pageable pageable);
    List<Hotel> findByHotelCategoryId(int categoryId);
    @Query(value = "SELECT COUNT(*) FROM hotels", nativeQuery = true)
    long countTotalHotels();


}
