package ru.hogwarts.school.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.hogwarts.school.dto.FacultyDtoIn;
import ru.hogwarts.school.dto.FacultyDtoOut;
import ru.hogwarts.school.dto.StudentDtoOut;
import ru.hogwarts.school.mapper.FacultyMapper;
import ru.hogwarts.school.mapper.StudentMapper;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.repository.FacultyRepo;
import ru.hogwarts.school.repository.StudentRepo;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service

public class FacultyService {

    private final FacultyRepo facultyRepo;
    private final StudentRepo studentRepo;
    private final FacultyMapper facultyMapper;
    private final StudentMapper studentMapper;
    private final Logger logger = LoggerFactory.getLogger(FacultyService.class);

    public FacultyService(FacultyRepo facultyRepo, StudentRepo studentRepo, FacultyMapper facultyMapper, StudentMapper studentMapper) {
        this.facultyRepo = facultyRepo;
        this.studentRepo = studentRepo;
        this.facultyMapper = facultyMapper;
        this.studentMapper = studentMapper;
    }

    public FacultyDtoOut createFaculty(FacultyDtoIn facultyDtoIn) {
        logger.debug("createFaculty is executed");
        return facultyMapper.toDto(facultyRepo.save(facultyMapper.toEntity(facultyDtoIn)));
    }

    public FacultyDtoOut editFaculty(long id, FacultyDtoIn facultyDtoIn) {
        logger.debug("editFaculty is executed");
        return facultyRepo.findById(id)
                .map(oldFaculty -> {
                    oldFaculty.setName(facultyDtoIn.getName());
                    oldFaculty.setColor(facultyDtoIn.getColor());
                    return facultyMapper.toDto(facultyRepo.save(oldFaculty));
                }).orElseThrow();
    }

    public FacultyDtoOut deleteFaculty(Long id) {
        logger.debug("deleteFaculty is executed");
        Faculty faculty = facultyRepo.findById(id).orElseThrow();
        facultyRepo.delete(faculty);
        return facultyMapper.toDto(faculty);
    }


    public FacultyDtoOut findFaculty(Long id) {
        logger.debug("findFaculty is executed");
        return facultyRepo.findById(id)
                .map(facultyMapper::toDto)
                .orElseThrow();
    }

    public List<FacultyDtoOut> findByName(String name) {
        logger.debug("findByName is executed");
        return facultyRepo.findAllByNameContainingIgnoreCase(name)
                .stream()
                .map(facultyMapper::toDto)
                .collect(Collectors.toList());
    }

    public List<StudentDtoOut> getStudentsInFaculty(long id) {
        logger.debug("getStudentsInFaculty is executed");
        return studentRepo.findAllByFacultyId(id).stream()
                .map(studentMapper::toDto)
                .collect(Collectors.toList());
    }

    public String getLongestName() {
        return facultyRepo.findAll()
                .stream()
                .map(Faculty::getName)
                .max(Comparator.comparingInt(String::length))
                .orElseThrow();
    }

}
