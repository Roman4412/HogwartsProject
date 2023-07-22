package ru.hogwarts.school.controller;

import org.springframework.web.bind.annotation.*;
import ru.hogwarts.school.dto.FacultyDtoOut;
import ru.hogwarts.school.dto.StudentDtoIn;
import ru.hogwarts.school.dto.StudentDtoOut;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.service.StudentService;

import java.util.List;

@RestController
@RequestMapping("/student")
public class StudentController {
    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @PostMapping
    public StudentDtoOut createStudent(@RequestBody StudentDtoIn studentDtoIn) {
        return studentService.createStudent(studentDtoIn);
    }

    @GetMapping("{id}")
    public StudentDtoOut getStudent(@PathVariable Long id) {
        return studentService.findStudent(id);
    }

    @GetMapping("/filter/{age}")
    public List<StudentDtoOut> findByAge(@PathVariable int age) {
        return studentService.findAllByAge(age);
    }

    @GetMapping("/filter")
    public List<StudentDtoOut> findByAgeBetween(@RequestParam int ageFrom, @RequestParam int ageTo) {
        return studentService.findByAgeBetween(ageFrom, ageTo);
    }

    @GetMapping("/{id}/faculty")
    public FacultyDtoOut getFaculty(@PathVariable("id") long id) {
        return studentService.getFaculty(id);
    }

    @PutMapping("{id}")
    public StudentDtoOut editStudent(@PathVariable Long id, @RequestBody StudentDtoIn studentDtoIn) {
        return studentService.editStudent(id, studentDtoIn);
    }

    @DeleteMapping("{id}")
    public StudentDtoOut deleteStudent(@PathVariable Long id) {
        return studentService.deleteStudent(id);
    }

    @GetMapping("/all")
    public List<Student> getAll() {
        return studentService.getAll();
    }

    @GetMapping("/all/average-age")
    public int getAverageAge() {
        return studentService.getAverageAge();
    }

    @GetMapping("/all/last-five")
    public List<Student> getLastFiveStudents() {
        return studentService.getLastFiveStudents();
    }

    @GetMapping("/all/starting-a-name")
    public List<Student> getStudentWhoseNameStartsWithA() {
        return studentService.getStudentWhoseNameStartsWithA();
    }

    @GetMapping("/stream")
    public String getStream() {
        return studentService.getStream();
    }
}
