package com.minor.photo_app.repository;

import com.minor.photo_app.entity.RefreshToken;
import com.minor.photo_app.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    Optional<RefreshToken> findByToken(@Param("token") String token);

    void deleteAllByUser(@Param("user") User user);
}
