package com.minor.photo_app.repository;

import com.minor.photo_app.entity.Category;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    @EntityGraph(attributePaths = {"children"})
    List<Category> findByParentIsNull();

    @EntityGraph(attributePaths = {"places"})
    Optional<Category> findByIdWithPlaces(@Param("id")Long id);
}
