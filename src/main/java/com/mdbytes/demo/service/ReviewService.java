package com.mdbytes.demo.service;

import com.mdbytes.demo.entity.Review;

import java.util.Optional;

public interface ReviewService {

    Review save(Review review);

    Optional<Review> findById(Integer id);

    void deleteById(Integer id);

    void deleteAll();

}
