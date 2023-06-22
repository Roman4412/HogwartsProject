package ru.hogwarts.school.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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

    @GetMapping("{id}")
    public ResponseEntity<Student> getStudent(@PathVariable Long id) {
        return ResponseEntity.ok(studentService.findStudent(id));
    }

    @PostMapping
    public ResponseEntity<Student> createStudent(@RequestBody Student student) {
        return ResponseEntity.ok(studentService.createStudent(student));
    }

    @DeleteMapping("{id}")
    public ResponseEntity deleteStudent(@PathVariable Long id) {
        studentService.deleteStudent(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping
    public ResponseEntity<Student> editStudent(@RequestBody Student student) {
        return ResponseEntity.ok(studentService.editStudent(student));
    }

    @GetMapping("/filter/{age}")
    public ResponseEntity<List<Student>> findByAge(@PathVariable int age) {
        return ResponseEntity.ok(studentService.findByAge(age));
    }

    @GetMapping
    public ResponseEntity getAllStudents() {
       return ResponseEntity.ok(studentService.getAllStudents());
    }
}
