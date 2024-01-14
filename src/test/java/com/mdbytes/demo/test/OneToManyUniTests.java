package com.mdbytes.demo.test;

import com.mdbytes.demo.entity.Course;
import com.mdbytes.demo.entity.Instructor;
import com.mdbytes.demo.entity.InstructorDetail;
import com.mdbytes.demo.entity.Review;
import com.mdbytes.demo.repository.CourseRepository;
import com.mdbytes.demo.repository.ReviewRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class OneToManyUniTests {

    private CourseRepository courseRepository;

    private ReviewRepository reviewRepository;

    private Course savedCourse;

    @Autowired
    public OneToManyUniTests(CourseRepository courseRepository, ReviewRepository reviewRepository) {
        this.courseRepository = courseRepository;
        this.reviewRepository = reviewRepository;
    }

    @AfterEach
    public void clearData() {
        courseRepository.deleteAll();
        reviewRepository.deleteAll();
    }

    @BeforeEach
    public void setUpData() {
        Instructor instructor = Instructor.builder()
                .firstName("Martin")
                .lastName("Dwyer")
                .email("martin@mdbytes.com")
                .detail(new InstructorDetail("http://youtube.com/mdbytes", "hiking"))
                .build();
        Course course = Course.builder()
                .title("Economics")
                .instructor(instructor)
                .build();
        savedCourse = courseRepository.save(course);

        Review reviewOne = new Review("Really great course!", course);
        Review reviewTwo = new Review("Really poor course!", course);
        Review reviewThree = new Review("Really great teacher!", course);
        Review reviewFour = new Review("OMG! This course is crazy", course);
        reviewRepository.save(reviewOne);
        reviewRepository.save(reviewTwo);
        reviewRepository.save(reviewThree);
        reviewRepository.save(reviewFour);
    }

    @Test
    public void OneToManyUni_Create_Course_Auto_Generates_Id() {


        // saved instructor has an id not null and greater than zero
        Assertions.assertThat(savedCourse.getId()).isGreaterThan(0);
    }

    @Test
    public void OneToManyUni_Create_Review_For_Courses() {

        List<Review> reviews = reviewRepository.findAll();

        // There are four reviews saved for the saved course
        Assertions.assertThat(reviews.size()).isEqualTo(4);

    }

    @Test
    public void OneToManyUni_Delete_Course_Deletes_Reviews() {

        Integer id = savedCourse.getId();

        courseRepository.deleteById(id);

        Course deletedCourse = null;
        Optional<Course> optCourse = courseRepository.findById(id);
        if (optCourse.isPresent()) {
            deletedCourse = optCourse.get();
        }

        List<Review> deletedCourseReviews = new ArrayList<>();
        Optional<List<Review>> optCourseReviewList = reviewRepository.findReviewsByCourseId(id);
        if (optCourseReviewList.isPresent()) {
            deletedCourseReviews = optCourseReviewList.get();
        }

        // There are no reviews saved for the deleted course
        Assertions.assertThat(deletedCourse).isNull();
        Assertions.assertThat(deletedCourseReviews.size()).isEqualTo(0);

    }

    @Test
    public void OneToManyUni_Delete_Review_Does_Not_Remove_Course() {
        reviewRepository.deleteById(1);
        Course course = courseRepository.findById(savedCourse.getId()).get();
        Optional<Course> optCourse = courseRepository.findById(savedCourse.getId());
        if (optCourse.isPresent()) {
            course = optCourse.get();
        }
        Assertions.assertThat(course).isNotNull();
    }
}

