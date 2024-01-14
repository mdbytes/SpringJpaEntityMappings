package com.mdbytes.demo.service;

import com.mdbytes.demo.entity.Instructor;

import java.util.Optional;

public interface InstructorService {

    void deleteInstructorById(Integer id);

    Instructor save(Instructor instructor);

    Optional<Instructor> findById(Integer id);

    void deleteById(Integer id);

    void delete(Instructor instructor);

    void deleteAll();
}
