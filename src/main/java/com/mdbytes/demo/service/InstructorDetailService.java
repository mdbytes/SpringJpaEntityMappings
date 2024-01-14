package com.mdbytes.demo.service;

import com.mdbytes.demo.entity.InstructorDetail;

import java.util.Optional;

public interface InstructorDetailService {

    InstructorDetail save(InstructorDetail detail);

    Optional<InstructorDetail> findById(Integer id);

    void deleteById(Integer id);

    void delete(InstructorDetail detail);

    void deleteAll();

}
