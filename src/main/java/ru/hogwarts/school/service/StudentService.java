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
import java.util.stream.IntStream;
import java.util.stream.Stream;

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

    public String getStream() {
        long t1 = System.nanoTime();
        int sum1 = Stream.iterate(1, a -> a + 1).limit(1_000_000).reduce(0, Integer::sum);
        long defaultTime = System.nanoTime() - t1;


        long t2 = System.nanoTime();
        int sum2 = IntStream.iterate(1, a -> a + 1).limit(1_000_000).reduce(0, Integer::sum);
        long improvedTime = System.nanoTime() - t2;

        return "sum1 = " + sum1
                + "\n"
                + "sum2 = " + sum2
                + "\n"
                + "default time = " + defaultTime
                + "\n"
                + "improvedTime = " + improvedTime;
    }

    public void getStudents() {
        List<Student> students = studentRepo.getAll();

        Thread thread1 = new Thread(() -> {
            logger.info("thread 1 started");
            printName(students, 2);
            printName(students, 3);
        });

        Thread thread2 = new Thread(() -> {
            logger.info("thread 2 started");
            printName(students, 4);
            printName(students, 5);
        });

        logger.info("thread main started");
        printName(students, 0);
        printName(students, 1);
        thread1.start();
        thread2.start();
    }


    public void getStudentsSync() {
        List<Student> students = studentRepo.getAll();

        Thread thread1 = new Thread(() -> {
            logger.info("thread 1 starting");
            printNameSync(students, 2);
            printNameSync(students, 3);
            logger.info("thread 1 ending");
        });

        Thread thread2 = new Thread(() -> {
            logger.info("thread 2 starting");
            printNameSync(students, 4);
            printNameSync(students, 5);
            logger.info("thread 2 ending");
        });

        thread1.start();
        thread2.start();
        logger.info("thread main starting");
        printNameSync(students, 0);
        printNameSync(students, 1);
        logger.info("thread main ending");
    }

    public void printName(List<Student> collection, int index) {
        System.out.println(collection.get(index).getName());
    }

    public synchronized void printNameSync(List<Student> collection, int index) {
        System.out.println(collection.get(index).getName());
    }
}
