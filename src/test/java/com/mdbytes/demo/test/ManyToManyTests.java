package com.mdbytes.demo.test;

import com.mdbytes.demo.entity.Course;
import com.mdbytes.demo.entity.Instructor;
import com.mdbytes.demo.entity.InstructorDetail;
import com.mdbytes.demo.entity.Student;
import com.mdbytes.demo.service.CourseService;
import com.mdbytes.demo.service.StudentService;
import jakarta.transaction.Transactional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import java.util.List;
import java.util.Optional;

@SpringBootTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class ManyToManyTests {
    private StudentService studentService;

    private CourseService courseService;

    private Course savedCourseOne;

    private String courseOneTitle = "Spring and Spring Boot";

    @Autowired
    public ManyToManyTests(StudentService studentService, CourseService courseService) {
        this.studentService = studentService;
        this.courseService = courseService;
    }

    @BeforeEach
    public void setUpTesting() {

        // Create course
        Course courseOne = new Course(courseOneTitle);

        // create students
        Student studentOne = Student.builder()
                .firstName("Martin")
                .lastName("Dwyer")
                .email("martin@md.com")
                .build();
        Student studentTwo = Student.builder()
                .firstName("Rose")
                .lastName("Dwyer")
                .email("rose@md.com")
                .build();
        Student studentThree = Student.builder()
                .firstName("Noah")
                .lastName("Dwyer")
                .email("noah@md.com")
                .build();

        // add instructor to course
        courseOne.setInstructor(Instructor.builder()
                .firstName("Martin")
                .lastName("Dwyer")
                .email("md@md.ccom")
                .detail(new InstructorDetail("http://youtube.com", "coding"))
                .build());

        // add students to the course
        courseOne.addStudent(studentOne);
        courseOne.addStudent(studentTwo);
        courseOne.addStudent(studentThree);

        // save the course and students
        savedCourseOne = courseService.save(courseOne);
    }

    @AfterEach
    public void afterEachTest() {

        courseService.deleteAll();
        studentService.deleteAll();
    }

    // Create object tests

    @Test
    public void manyToMany_Setup_Creates_Course() {
        Course springBootCourse = courseService.findCourseByTitle("Spring and Spring Boot");
        Assertions.assertThat(springBootCourse).isNotNull();
    }

    @Test
    public void manyToMany_Setup_Creates_Course_Adding_Students() {
        List<Student> students = studentService.findAll();
        Assertions.assertThat(students.size()).isEqualTo(3);
    }

    @Test
    public void manyToMany_Setup_Creates_Three_Students_CourseOne() {
        Course course = courseService.findCourseByTitle(courseOneTitle);
        List<Student> students = course.getStudents();
        Assertions.assertThat(students.size()).isEqualTo(3);
    }

    @Test
    public void manyToMany_Setup_Creates_CourseTwo() {
        Course course = new Course("Spring Jpa Data Rest");
        course.setInstructor(Instructor.builder()
                .firstName("Martin")
                .lastName("Dwyer")
                .email("md@md.ccom")
                .detail(new InstructorDetail("http://youtube.com", "coding"))
                .build());
        course.addStudent(Student.builder()
                .firstName("Marty")
                .lastName("Dwyer")
                .email("marty@md.com")
                .build());
        courseService.save(course);

        Course newCourse = courseService.findCourseByTitle("Spring Jpa Data Rest");

        Assertions.assertThat(newCourse).isNotNull();
    }

    @Test
    public void manyToMany_Setup_Adds_Saved_Student_To_CourseTwo() {
        Course course = new Course("Spring Jpa Data Rest");
        course.setInstructor(Instructor.builder()
                .firstName("Martin")
                .lastName("Dwyer")
                .email("md@md.ccom")
                .detail(new InstructorDetail("http://youtube.com", "coding"))
                .build());
        course.addStudent(Student.builder()
                .firstName("Marty")
                .lastName("Dwyer")
                .email("marty@md.com")
                .build());

        courseService.save(course);

        Student studentOne = studentService.findAll().get(0);
        course.addStudent(studentOne);

        courseService.save(course);

        Course newCourse = courseService.findCourseByTitle("Spring Jpa Data Rest");
        List<Student> students = studentService.findStudentsByCourseId(newCourse.getId());

        Assertions.assertThat(students.size()).isEqualTo(2);
    }

    // Retrieve object tests

    @Test
    public void manyToMany_Retrieves_Course_Correctly() {
        Course springBootCourse = courseService.findCourseByTitle("Spring and Spring Boot");
        Assertions.assertThat(springBootCourse.getTitle()).isEqualTo("Spring and Spring Boot");
    }


    // Update Tests

    @Test
    public void manyToMany_Update_Student_Updates_Student() {

        Student theStudent = studentService.findByEmail("martin@md.com").get();
        theStudent.setFirstName("Marty");
        Student theSavedStudent = studentService.save(theStudent);

        Assertions.assertThat(theSavedStudent.getFirstName()).isEqualTo("Marty");
    }

    @Test
    @Transactional
    public void manyToMany_Update_Student_Updates_Class() {
        // Get a student from the database
        Student theStudent = studentService.findByEmail("martin@md.com").get();

        // Update student's first name
        theStudent.setFirstName("Marty");
        studentService.save(theStudent);

        // Get a course the student is in from the database
        Course springBootCourse = courseService.findCourseByTitle("Spring and Spring Boot");

        // Extract the student from the list of course students
        Student student = null;
        Optional<Student> optStudent = springBootCourse.getStudents().stream().filter(s -> s.getEmail().equals("martin@md.com")).findFirst();
        if (optStudent.isPresent()) {
            student = optStudent.get();
        }

        // Verify that the change in the student populated in the course student list
        Assertions.assertThat(student.getFirstName()).isEqualTo("Marty");
    }

    // Delete Tests

    @Test
    public void manyToMany_Delete_Course_Removes_Course() {
        courseService.deleteById(savedCourseOne.getId());
        Course course = null;
        Optional<Course> optCourse = courseService.findById(savedCourseOne.getId());
        if (optCourse.isPresent()) {
            course = optCourse.get();
        }
        Assertions.assertThat(course).isNull();
    }

    @Test
    public void manyToMany_Delete_Course_Leaves_Students() {
        courseService.deleteById(savedCourseOne.getId());
        Assertions.assertThat(studentService.findAll().size()).isEqualTo(3);

    }

}
