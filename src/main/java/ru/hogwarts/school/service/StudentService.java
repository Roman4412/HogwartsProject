package ru.hogwarts.school.service;

import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.StudentRepo;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class StudentService {
    private final StudentRepo studentRepo;

    public StudentService(StudentRepo studentRepo) {
        this.studentRepo = studentRepo;
    }

    public Student createStudent(Student student) {
        return studentRepo.save(student);
    }

    public void deleteStudent(Long id) {
        studentRepo.deleteById(id);
    }

    public Student editStudent(Student student) {
        return studentRepo.save(student);
    }

    public Student findStudent(Long id) {
        return studentRepo.findById(id).orElseThrow();
    }
    public List<Student> findByAge(int age) {
        return studentRepo.findAllByAgeIs(age);
    }

    public Collection<Student> getAllStudents() {
        return studentRepo.findAll();
    }
}
