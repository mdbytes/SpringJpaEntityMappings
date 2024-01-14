package com.mdbytes.demo.service.impl;

import com.mdbytes.demo.entity.Course;
import com.mdbytes.demo.entity.Instructor;
import com.mdbytes.demo.repository.CourseRepository;
import com.mdbytes.demo.repository.InstructorRepository;
import com.mdbytes.demo.service.InstructorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class InstructorServiceImpl implements InstructorService {

    private InstructorRepository instructorRepository;
    private CourseRepository courseRepository;

    @Autowired
    public InstructorServiceImpl(InstructorRepository instructorRepository, CourseRepository courseRepository) {
        this.instructorRepository = instructorRepository;
        this.courseRepository = courseRepository;
    }

    @Override
    public void deleteInstructorById(Integer id) {
        Instructor instructor = instructorRepository.findById(id).get();
        List<Course> instructorCourses = new ArrayList<>();

        Optional<List<Course>> optionalCourses = courseRepository.findCoursesByInstructorId(id);

        if (optionalCourses.isPresent()) {
            instructorCourses = optionalCourses.get();
            for (Course c : instructorCourses) {
                c.setInstructor(null);
                instructor.getCourses().remove(c);
                courseRepository.save(c);

            }
        }
        instructor.setCourses(new ArrayList<>());
        instructorRepository.save(instructor);
        instructorRepository.delete(instructor);

    }

    @Override
    public Instructor save(Instructor instructor) {
        return instructorRepository.save(instructor);
    }

    @Override
    public Optional<Instructor> findById(Integer id) {
        return instructorRepository.findById(id);
    }

    @Override
    public void deleteById(Integer id) {
        instructorRepository.deleteById(id);
    }

    @Override
    public void delete(Instructor instructor) {
        instructorRepository.delete(instructor);
    }

    @Override
    public void deleteAll() {
        instructorRepository.deleteAll();
    }
}
