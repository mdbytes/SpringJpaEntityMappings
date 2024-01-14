package com.mdbytes.demo.service.impl;

import com.mdbytes.demo.entity.Student;
import com.mdbytes.demo.repository.CourseRepository;
import com.mdbytes.demo.repository.StudentRepository;
import com.mdbytes.demo.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class StudentServiceImpl implements StudentService {

    private CourseRepository courseRepository;

    private StudentRepository studentRepository;

    @Autowired
    public StudentServiceImpl(CourseRepository courseRepository, StudentRepository studentRepository) {
        this.courseRepository = courseRepository;
        this.studentRepository = studentRepository;
    }

    @Override
    public Student save(Student student) {
        return studentRepository.save(student);
    }

    @Override
    public Optional<Student> findById(Integer id) {
        return studentRepository.findById(id);
    }

    @Override
    public Optional<Student> findByEmail(String email) {
        return studentRepository.findStudentByEmail(email);
    }

    @Override
    public List<Student> findAll() {
        return studentRepository.findAll();
    }

    @Override
    public List<Student> findStudentsByCourseId(Integer id) {
        return courseRepository.findStudentsByCourseId(id);
    }

    @Override
    public void deleteById(Integer id) {
        studentRepository.deleteById(id);
    }

    @Override
    public void deleteAll() {
        for (Student s : studentRepository.findAll()) {
            s.setCourses(new ArrayList<>());
            studentRepository.save(s);
        }
        studentRepository.deleteAll();
    }

}
