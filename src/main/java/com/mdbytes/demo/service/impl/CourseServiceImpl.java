package com.mdbytes.demo.service.impl;

import com.mdbytes.demo.entity.Course;
import com.mdbytes.demo.repository.CourseRepository;
import com.mdbytes.demo.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CourseServiceImpl implements CourseService {

    private CourseRepository courseRepository;

    @Autowired
    public CourseServiceImpl(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    @Override
    public Course save(Course course) {
        return courseRepository.save(course);
    }

    @Override
    public Optional<Course> findById(Integer id) {
        return courseRepository.findById(id);
    }

    @Override
    public List<Course> findAll() {
        return courseRepository.findAll();
    }

    @Override
    public void deleteById(Integer id) {
        Course course = courseRepository.findById(id).get();
        course.setInstructor(null);
        course.setStudents(new ArrayList<>());
        courseRepository.save(course);
        courseRepository.deleteById(id);
    }

    @Override
    public void deleteAll() {
        for (Course c : courseRepository.findAll()) {
            c.setStudents(new ArrayList<>());
            courseRepository.save(c);
        }
        courseRepository.deleteAll();
    }

    @Override
    public Optional<List<Course>> findCoursesByInstructorId(Integer id) {
        return courseRepository.findCoursesByInstructorId(id);
    }

    @Override
    public Course findCourseByTitle(String springAndSpringBoot) {
        return courseRepository.findFirstCourseByTitle(springAndSpringBoot);
    }


}
