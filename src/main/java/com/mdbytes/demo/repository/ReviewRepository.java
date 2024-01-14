package com.mdbytes.demo.repository;

import com.mdbytes.demo.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Integer> {

    Optional<List<Review>> findReviewsByCourseId(Integer id);
}
