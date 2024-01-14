package com.mdbytes.demo.service;

import com.mdbytes.demo.entity.Student;

import java.util.List;
import java.util.Optional;

public interface StudentService {

    Student save(Student student);

    Optional<Student> findById(Integer id);

    Optional<Student> findByEmail(String email);

    List<Student> findAll();

    List<Student> findStudentsByCourseId(Integer id);

    void deleteById(Integer id);

    void deleteAll();

}
