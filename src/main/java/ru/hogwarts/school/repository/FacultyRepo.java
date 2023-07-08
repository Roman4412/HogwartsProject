package ru.hogwarts.school.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.hogwarts.school.model.Faculty;

import java.util.List;

public interface FacultyRepo extends JpaRepository<Faculty, Long> {
    List<Faculty> findAllByNameContainingIgnoreCase(String name);

}
