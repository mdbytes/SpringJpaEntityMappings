package com.mdbytes.demo.test;

import com.mdbytes.demo.entity.Course;
import com.mdbytes.demo.entity.Instructor;
import com.mdbytes.demo.entity.InstructorDetail;
import com.mdbytes.demo.service.CourseService;
import com.mdbytes.demo.service.InstructorDetailService;
import com.mdbytes.demo.service.InstructorService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@SpringBootTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class OneToManyTests {

    private CourseService courseService;

    private InstructorService instructorService;

    private InstructorDetailService instructorDetailService;
    private Course savedCourse;

    private Instructor savedInstructor;

    @Autowired
    public OneToManyTests(CourseService courseService, InstructorService instructorService, InstructorDetailService instructorDetailService) {
        this.courseService = courseService;
        this.instructorService = instructorService;
        this.instructorDetailService = instructorDetailService;
    }

    @AfterEach
    public void clearData() {
        courseService.deleteAll();
        instructorService.deleteAll();
    }

    @BeforeEach
    public void setUpData() {
        InstructorDetail detail = new InstructorDetail("http://youtube.com/mdbytes", "hiking");
        Instructor instructor = Instructor.builder()
                .firstName("Martin")
                .lastName("Dwyer")
                .email("martin@mdbytes.com")
                .detail(detail)
                .build();
        savedInstructor = instructorService.save(instructor);

        Course course = new Course("Economics");
        courseService.save(course);
        course.setInstructor(savedInstructor);
        savedCourse = courseService.save(course);


    }

    @Test
    public void OneToMany_Create_Course_Auto_Generates_Course_Id() {

        // saved instructor has an id not null and greater than zero
        Assertions.assertThat(savedCourse.getId()).isGreaterThan(0);
    }

    @Test
    public void OneToMany_Create_Course_Auto_Generates_Instructor_Id() {

        // saved instructor has an id not null and greater than zero
        Assertions.assertThat(savedInstructor.getId()).isGreaterThan(0);
    }

    @Test
    public void OneToMany_Create_Course_Included_In_Instructor_Courses() {

        List<Course> instructorCourses = new ArrayList<>();
        Optional<List<Course>> optionalCourses = courseService.findCoursesByInstructorId(savedInstructor.getId());
        if (optionalCourses.isPresent()) {
            instructorCourses = optionalCourses.get();
        }

        // Instructor courses includes the added course
        Assertions.assertThat(instructorCourses.size()).isEqualTo(1);
    }

    @Test
    public void OneToMany_Delete_Course_Will_Not_Delete_Instructor() {

        Integer courseId = savedCourse.getId();
        Integer instructorId = savedInstructor.getId();

        courseService.deleteById(courseId);

        Course deletedCourse = null;
        Optional<Course> optCourse = courseService.findById(courseId);
        if (optCourse.isPresent()) {
            deletedCourse = optCourse.get();
        }

        Instructor verifiedInstructor = null;
        Optional<Instructor> optionalInstructor = instructorService.findById(instructorId);
        if (optionalInstructor.isPresent()) {
            verifiedInstructor = optionalInstructor.get();
        }

        // The deleted course is gone
        Assertions.assertThat(deletedCourse).isNull();

        System.out.println(verifiedInstructor);

        // The instructor is still there
        Assertions.assertThat(verifiedInstructor).isNotNull();

        // The instructor courses is now empty
        List<Course> instructorCourses = new ArrayList<>();
        Optional<List<Course>> optionalCourses = courseService.findCoursesByInstructorId(savedInstructor.getId());
        if (optionalCourses.isPresent()) {
            instructorCourses = optionalCourses.get();
        }
        Assertions.assertThat(instructorCourses.size()).isEqualTo(0);

    }

    @Test
    public void OneToMany_Deleted_Instructor_Is_Null() {

        Integer courseId = savedCourse.getId();
        Integer instructorId = savedInstructor.getId();

        instructorService.deleteInstructorById(instructorId);

        Instructor deletedInstructor = null;
        Optional<Instructor> optInstructor = instructorService.findById(instructorId);
        System.out.println("\noptInstructor");
        System.out.println(optInstructor);
        if (optInstructor.isPresent()) {
            deletedInstructor = optInstructor.get();
        }

        Course verifiedCourse = null;
        Optional<Course> optionalCourse = courseService.findById(courseId);
        if (optionalCourse.isPresent()) {
            verifiedCourse = optionalCourse.get();
        }

        // The deleted instructor is gone
        Assertions.assertThat(deletedInstructor).isNull();

    }

    @Test
    public void OneToMany_Deleted_Instructor_Will_Not_Delete_Course() {

        Integer courseId = savedCourse.getId();
        Integer instructorId = savedInstructor.getId();

        List<Course> courseList = new ArrayList<>();
        Optional<List<Course>> optionalCourses = courseService.findCoursesByInstructorId(instructorId);
        if (optionalCourses.isPresent()) {
            for (Course c : optionalCourses.get()) {
                c.setInstructor(null);
            }
        }
        instructorService.deleteInstructorById(instructorId);

        Instructor deletedInstructor = null;
        Optional<Instructor> optInstructor = instructorService.findById(instructorId);
        if (optInstructor.isPresent()) {
            deletedInstructor = optInstructor.get();
        }

        Course verifiedCourse = null;
        Optional<Course> optionalCourse = courseService.findById(courseId);
        if (optionalCourse.isPresent()) {
            verifiedCourse = optionalCourse.get();
        }

        // The course is still there
        Assertions.assertThat(verifiedCourse).isNotNull();

    }

    @Test
    public void OneToMany_Deleted_Instructor_Leaves_Null_Instructor() {

        Integer courseId = savedCourse.getId();
        Integer instructorId = savedInstructor.getId();

        instructorService.deleteInstructorById(instructorId);

        Instructor deletedInstructor = null;
        Optional<Instructor> optInstructor = instructorService.findById(instructorId);
        if (optInstructor.isPresent()) {
            deletedInstructor = optInstructor.get();
        }

        Course verifiedCourse = null;
        Optional<Course> optionalCourse = courseService.findById(courseId);
        if (optionalCourse.isPresent()) {
            verifiedCourse = optionalCourse.get();
        }

        //System.err.println(verifiedCourse);

        // The course instructor is null
        Assertions.assertThat(verifiedCourse.getInstructor()).isNull();

    }

}

