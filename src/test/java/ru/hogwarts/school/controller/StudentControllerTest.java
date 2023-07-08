package ru.hogwarts.school.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.hogwarts.school.dto.StudentDtoIn;
import ru.hogwarts.school.dto.StudentDtoOut;
import ru.hogwarts.school.mapper.StudentMapper;
import ru.hogwarts.school.repository.StudentRepo;

import static org.assertj.core.api.Assertions.*;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class StudentControllerTest {
    @LocalServerPort
    private int port;

    @Autowired
    private StudentController studentController;

    @Autowired
    private TestRestTemplate testRestTemplate;
    @Autowired
    private StudentRepo studentRepo;

    @Autowired
    private StudentMapper studentMapper;

    @Test
    public void contextLoads() {
        assertThat(studentController).isNotNull();
    }

    @Test
    public void createTest() {
        StudentDtoIn newStudent = createDtoIn();
        HttpEntity<StudentDtoIn> entity = new HttpEntity<>(newStudent);
        ResponseEntity<StudentDtoOut> response =
                this.testRestTemplate.exchange(
                        "/student",
                        HttpMethod.POST,
                        entity,
                        StudentDtoOut.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getName()).isEqualTo(newStudent.getName());
        assertThat(response.getBody().getAge()).isEqualTo(newStudent.getAge());
        studentRepo.delete(studentMapper.toEntity(newStudent));
    }

    @Test
    public void getStudentTest() {
        StudentDtoIn newStudent = createDtoIn();
        long id = studentRepo.save(studentMapper.toEntity(newStudent)).getId();
        HttpEntity<StudentDtoIn> entity = new HttpEntity<>(newStudent);
        ResponseEntity<StudentDtoOut> response =
                this.testRestTemplate.exchange(
                        "/student/" + id,
                        HttpMethod.GET,
                        entity,
                        StudentDtoOut.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getId()).isEqualTo(id);
        assertThat(response.getBody().getName()).isEqualTo(newStudent.getName());
        studentRepo.delete(studentRepo.findById(id).get());
    }

    @Test
    public void deleteTest() {
        StudentDtoIn newStudent = createDtoIn();
        long id = studentRepo.save(studentMapper.toEntity(newStudent)).getId();
        HttpEntity<StudentDtoIn> entity = new HttpEntity<>(newStudent);
        ResponseEntity<StudentDtoOut> response =
                this.testRestTemplate.exchange(
                        "/student/" + id,
                        HttpMethod.DELETE,
                        entity,
                        StudentDtoOut.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getId()).isEqualTo(id);
        assertThat(response.getBody().getName()).isEqualTo(newStudent.getName());
    }

    @Test
    public void editTest() {
        StudentDtoIn newStudent = createDtoIn();
        newStudent.setAge(-999);
        newStudent.setName("newTestStudent");
        StudentDtoIn oldStudent = createDtoIn();
        long id = studentRepo.save(studentMapper.toEntity(oldStudent)).getId();

        HttpEntity<StudentDtoIn> entity = new HttpEntity<>(newStudent);
        ResponseEntity<StudentDtoOut> response =
                this.testRestTemplate.exchange(
                        "/student/" + id,
                        HttpMethod.PUT,
                        entity,
                        StudentDtoOut.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getId()).isEqualTo(id);
        assertThat(response.getBody().getName()).isEqualTo(newStudent.getName());
        studentRepo.delete(studentRepo.findById(id).get());
    }

    private StudentDtoIn createDtoIn() {
        StudentDtoIn studentDtoIn = new StudentDtoIn();
        studentDtoIn.setName("testStudent");
        studentDtoIn.setAge(9999);
        return studentDtoIn;
    }

}
