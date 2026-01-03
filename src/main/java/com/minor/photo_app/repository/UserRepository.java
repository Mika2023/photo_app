package com.minor.photo_app.repository;

import com.minor.photo_app.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Boolean existsByNickname(@Param("nickname") String nickname);

    Boolean existsByEmail(@Param("email") String email);

    Optional<User> findByNickname(@Param("nickname") String nickname);
}
