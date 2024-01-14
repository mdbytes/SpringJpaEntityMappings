package com.mdbytes.demo.test;

import com.mdbytes.demo.entity.Instructor;
import com.mdbytes.demo.entity.InstructorDetail;
import com.mdbytes.demo.repository.InstructorDetailRepository;
import com.mdbytes.demo.repository.InstructorRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;

import java.util.Optional;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class OneToOneTests {

    private InstructorRepository instructorRepository;

    private InstructorDetailRepository instructorDetailRepository;

    private Instructor instructor;

    private Instructor savedInstructor;

    @Autowired
    public OneToOneTests(InstructorRepository instructorRepository, InstructorDetailRepository instructorDetailRepository) {
        this.instructorRepository = instructorRepository;
        this.instructorDetailRepository = instructorDetailRepository;
    }

    @AfterEach
    public void clearData() {
        instructorRepository.deleteAll();
        instructorDetailRepository.deleteAll();
    }

    @BeforeEach
    public void setUpData() {
        instructor = Instructor.builder()
                .firstName("Martin")
                .lastName("Dwyer")
                .email("martin@mdbytes.com")
                .detail(new InstructorDetail("http://youtube.com/mdbytes", "hiking"))
                .build();
        savedInstructor = instructorRepository.save(instructor);
    }

    @Test
    public void OneToOneTests_CreateInstructor_Auto_Generated_Id() {
        // should be one instructor created in memory database
        System.out.println("Printing new instructor ...");
        System.out.println(instructor);
        Assertions.assertThat(instructorRepository.findAll().size()).isEqualTo(1);

        // saved instructor has an id not null and greater than zero
        Assertions.assertThat(savedInstructor.getId()).isNotNull().isGreaterThan(0);
    }

    @Test
    public void OneToOneTests_CreateInstructorDetail_Saved_to_DB_With_Instructor() {

        // instructor detail should be stored in a table seperately
        Assertions.assertThat(instructorDetailRepository.findAll().size()).isEqualTo(1);
    }

    @Test
    public void OneToOneTests_Delete_Instructor_Deletes_Instructor() {
        // delete instructor
        instructorRepository.delete(savedInstructor);

        // instructors should be empty
        Assertions.assertThat(instructorRepository.findAll().size()).isEqualTo(0);

    }

    @Test
    public void OneToOneTests_Delete_Instructor_Deletes_Detail() {
        // delete instructor
        instructorRepository.delete(savedInstructor);

        // instructor detail should also be empty
        Assertions.assertThat(instructorDetailRepository.findAll().size()).isEqualTo(0);
    }

    @Test
    public void OneToOneTests_Delete_Detail_Removes_Detail() {

        // delete instructor detail
        InstructorDetail savedDetail = savedInstructor.getDetail();
        instructorDetailRepository.delete(savedDetail);

        // instructor detail should also be empty
        InstructorDetail testDetail = null;
        Optional<InstructorDetail> detail = instructorDetailRepository.findById(savedDetail.getId());
        if (detail.isPresent()) {
            testDetail = detail.get();
        }
        Assertions.assertThat(testDetail).isNull();
    }

    @Test
    public void OneToOneTests_Delete_Detail_Does_Not_Removes_Instructor() {

        // delete instructor detail
        InstructorDetail savedDetail = savedInstructor.getDetail();
        instructorDetailRepository.delete(savedDetail);

        // instructors should be empty
        Assertions.assertThat(instructorRepository.findAll().size()).isEqualTo(1);

    }

}