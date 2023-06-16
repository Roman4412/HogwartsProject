package ru.hogwarts.school.service;

import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Faculty;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Service

public class FacultyService {
    private final HashMap<Long, Faculty> faculties = new HashMap<>();
    private Long lastId = 0L;

    public Faculty createFaculty(Faculty faculty) {
        faculties.put(++lastId, faculty);
        faculty.setId(lastId);
        return faculty;
    }

    public Faculty deleteFaculty(Long id) {
        return faculties.remove(id);
    }

    public Faculty editFaculty(Faculty faculty) {
        return faculties.put(faculty.getId(), faculty);
    }

    public Faculty findFaculty(Long id) {
        return faculties.get(id);
    }

    public List<Faculty> findByColor(String color) {
        return faculties.values().stream()
                .filter((faculty -> faculty.getColor().equals(color)))
                .collect(Collectors.toList());
    }
}
