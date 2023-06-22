package ru.hogwarts.school.service;

import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.repository.FacultyRepo;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service

public class FacultyService {

    private final FacultyRepo facultyRepo;

    public FacultyService(FacultyRepo facultyRepo) {
        this.facultyRepo = facultyRepo;
    }

    public Faculty createFaculty(Faculty faculty) {
        return facultyRepo.save(faculty);
    }

    public void deleteFaculty(Long id) {
        facultyRepo.deleteById(id);
    }

    public Faculty editFaculty(Faculty faculty) {
        return facultyRepo.save(faculty);
    }

    public Faculty findFaculty(Long id) {
       return facultyRepo.findById(id).orElseThrow();
    }

    public List<Faculty> findByColor(String color) {
        return facultyRepo.findAllByColorLikeIgnoreCase(color);
    }

    public Collection<Faculty> getAllFaculty() {
        return facultyRepo.findAll();
    }
}
