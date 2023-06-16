package ru.hogwarts.school.service;

import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Student;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class StudentService {
    private final HashMap<Long, Student> students = new HashMap<>();
    private Long lastId = 0L;

    public Student createStudent(Student student) {
        students.put(++lastId, student);
        student.setId(lastId);
        return student;
    }

    public Student deleteStudent(Long id) {
        return students.remove(id);
    }

    public Student editStudent(Student student) {
        return students.put(student.getId(), student);
    }

    public Student findStudent(Long id) {
        return students.get(id);
    }

    public List<Student> findByAge(int age) {
        return students.values().stream()
                .filter((student -> student.getAge() == age))
                .collect(Collectors.toList());
    }
}
