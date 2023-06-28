package ru.hogwarts.school.service;

import org.springframework.stereotype.Service;
import ru.hogwarts.school.dto.FacultyDtoOut;
import ru.hogwarts.school.dto.StudentDtoIn;
import ru.hogwarts.school.dto.StudentDtoOut;
import ru.hogwarts.school.mapper.FacultyMapper;
import ru.hogwarts.school.mapper.StudentMapper;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.FacultyRepo;
import ru.hogwarts.school.repository.StudentRepo;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class StudentService {
    private final StudentRepo studentRepo;
    private final FacultyRepo facultyRepo;
    private final StudentMapper studentMapper;
    private final FacultyMapper facultyMapper;

    public StudentService(StudentRepo studentRepo, FacultyRepo facultyRepo, StudentMapper studentMapper, FacultyMapper facultyMapper) {
        this.studentRepo = studentRepo;
        this.facultyRepo = facultyRepo;
        this.studentMapper = studentMapper;
        this.facultyMapper = facultyMapper;
    }

    public StudentDtoOut createStudent(StudentDtoIn studentDtoIn) {
        return studentMapper.toDto(studentRepo.save(studentMapper.toEntity(studentDtoIn)));
    }

    public StudentDtoOut editStudent(long id, StudentDtoIn studentDtoIn) {
        return studentRepo.findById(id)
                .map(oldStudent -> {
                    oldStudent.setName(studentDtoIn.getName());
                    oldStudent.setAge(studentDtoIn.getAge());
                    Optional.ofNullable(studentDtoIn.getFacultyId())
                            .ifPresent(facultyId ->
                                    oldStudent.setFaculty(facultyRepo.findById(facultyId)
                                    .orElseThrow()));
                    return studentMapper.toDto(studentRepo.save(oldStudent));
                }).orElseThrow();
    }

    public StudentDtoOut deleteStudent(Long id) {
        Student student = studentRepo.findById(id).orElseThrow();
        studentRepo.delete(student);
        return studentMapper.toDto(student);
    }


    public StudentDtoOut findStudent(Long id) {
        return studentRepo.findById(id).map(studentMapper::toDto).orElseThrow();
    }

    public List<StudentDtoOut> findAllByAge(Integer age) {
        return Optional.ofNullable(age)
                .map(studentRepo::findAllByAgeIs)
                .orElseGet(studentRepo::findAll).stream()
                .map(studentMapper::toDto)
                .collect(Collectors.toList());
    }

    public List<StudentDtoOut> findByAgeBetween(int ageFrom, int ageTo) {
        return studentRepo.findByAgeBetween(ageFrom, ageTo).stream()
                .map(studentMapper::toDto)
                .collect(Collectors.toList());
    }

    public FacultyDtoOut getFaculty(long id) {
        return studentRepo.findById(id)
                .map(Student::getFaculty)
                .map(facultyMapper::toDto)
                .orElseThrow();
    }
}
