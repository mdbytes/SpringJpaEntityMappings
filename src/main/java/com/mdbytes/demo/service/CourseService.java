package com.mdbytes.demo.service;

import com.mdbytes.demo.entity.Course;

import java.util.List;
import java.util.Optional;

public interface CourseService {

    Course save(Course course);

    Optional<Course> findById(Integer id);

    List<Course> findAll();

    void deleteById(Integer id);

    void deleteAll();

    Optional<List<Course>> findCoursesByInstructorId(Integer id);

    Course findCourseByTitle(String springAndSpringBoot);
}
