package ru.hogwarts.school.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.hogwarts.school.dto.FacultyDtoOut;
import ru.hogwarts.school.dto.StudentDtoIn;
import ru.hogwarts.school.dto.StudentDtoOut;
import ru.hogwarts.school.mapper.FacultyMapper;
import ru.hogwarts.school.mapper.StudentMapper;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.FacultyRepo;
import ru.hogwarts.school.repository.StudentRepo;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class StudentService {
    private final StudentRepo studentRepo;
    private final FacultyRepo facultyRepo;
    private final StudentMapper studentMapper;
    private final FacultyMapper facultyMapper;
    public final Logger logger = LoggerFactory.getLogger(StudentService.class);

    public StudentService(StudentRepo studentRepo,
                          FacultyRepo facultyRepo,
                          StudentMapper studentMapper,
                          FacultyMapper facultyMapper) {
        this.studentRepo = studentRepo;
        this.facultyRepo = facultyRepo;
        this.studentMapper = studentMapper;
        this.facultyMapper = facultyMapper;
    }

    public StudentDtoOut createStudent(StudentDtoIn studentDtoIn) {
        logger.debug("createStudent is executed");
        return studentMapper.toDto(studentRepo.save(studentMapper.toEntity(studentDtoIn)));
    }

    public StudentDtoOut editStudent(long id, StudentDtoIn studentDtoIn) {
        logger.debug("editStudent is executed");
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
        logger.debug("deleteStudent is executed");
        Student student = studentRepo.findById(id).orElseThrow();
        studentRepo.delete(student);
        return studentMapper.toDto(student);
    }


    public StudentDtoOut findStudent(Long id) {
        logger.debug("findStudent is executed");
        return studentRepo.findById(id).map(studentMapper::toDto).orElseThrow();
    }

    public List<StudentDtoOut> findAllByAge(Integer age) {
        logger.debug("findAllByAge is executed");
        return Optional.ofNullable(age)
                .map(studentRepo::findAllByAgeIs)
                .orElseGet(studentRepo::findAll).stream()
                .map(studentMapper::toDto)
                .collect(Collectors.toList());
    }

    public List<StudentDtoOut> findByAgeBetween(int ageFrom, int ageTo) {
        logger.debug("findByAgeBetween is executed");
        return studentRepo.findByAgeBetween(ageFrom, ageTo).stream()
                .map(studentMapper::toDto)
                .collect(Collectors.toList());
    }

    public FacultyDtoOut getFaculty(long id) {
        logger.debug("getFaculty is executed");
        return studentRepo.findById(id)
                .map(Student::getFaculty)
                .map(facultyMapper::toDto)
                .orElseThrow();
    }

    public List<Student> getAll() {
        logger.debug("getAll is executed");
        return studentRepo.getAll();
    }

    public int getAverageAge() {
        logger.debug("getAverageAge is executed");
        return (int) studentRepo.getAll().stream().mapToInt(Student::getAge).average().orElseThrow();
    }

    public List<Student> getLastFiveStudents() {
        logger.debug("getLastFiveStudents is executed");
        return studentRepo.getLastFiveStudents();
    }

    public Student findStudentForAvatar(long id) {
        return studentRepo.findById(id).orElseThrow();
    }

    public List<Student> getStudentWhoseNameStartsWithA() {
        logger.debug("getStudentWhoseNameStartsWithA is executed");
        return studentRepo.getAll()
                .stream()
                .filter(student -> student.getName().startsWith("A"))
                .sorted(Comparator.comparing(Student::getName))
                .collect(Collectors.toList());
    }
}
