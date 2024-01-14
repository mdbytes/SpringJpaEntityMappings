package com.mdbytes.demo.repository;

import com.mdbytes.demo.entity.Course;
import com.mdbytes.demo.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CourseRepository extends JpaRepository<Course, Integer> {
    Optional<List<Course>> findCoursesByInstructorId(Integer instructorId);

    Course findFirstCourseByTitle(String title);

    @Query("Select c.students FROM Course c WHERE c.id  = ?1")
    List<Student> findStudentsByCourseId(Integer id);
}
