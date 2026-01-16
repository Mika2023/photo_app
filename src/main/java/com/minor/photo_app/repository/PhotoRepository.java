package com.minor.photo_app.repository;

import com.minor.photo_app.entity.Photo;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PhotoRepository extends JpaRepository<Photo, Long> {
    Slice<Photo> getPhotosByUserId(Long userId);
}
