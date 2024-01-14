package com.mdbytes.demo.service.impl;

import com.mdbytes.demo.entity.InstructorDetail;
import com.mdbytes.demo.repository.InstructorDetailRepository;
import com.mdbytes.demo.service.InstructorDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class InstructorDetailServiceImpl implements InstructorDetailService {

    private InstructorDetailRepository instructorDetailRepository;

    @Autowired
    public InstructorDetailServiceImpl(InstructorDetailRepository instructorDetailRepository) {
        this.instructorDetailRepository = instructorDetailRepository;
    }

    @Override
    public InstructorDetail save(InstructorDetail detail) {
        return instructorDetailRepository.save(detail);
    }

    @Override
    public Optional<InstructorDetail> findById(Integer id) {
        return instructorDetailRepository.findById(id);
    }

    @Override
    public void deleteById(Integer id) {
        instructorDetailRepository.deleteById(id);
    }

    @Override
    public void delete(InstructorDetail detail) {
        instructorDetailRepository.delete(detail);
    }

    @Override
    public void deleteAll() {
        instructorDetailRepository.deleteAll();
    }
}
