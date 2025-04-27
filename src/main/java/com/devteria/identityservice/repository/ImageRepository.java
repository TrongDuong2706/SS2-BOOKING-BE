package com.devteria.identityservice.repository;

import com.devteria.identityservice.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image, Long> {
}
