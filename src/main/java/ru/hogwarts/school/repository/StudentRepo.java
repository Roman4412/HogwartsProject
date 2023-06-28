package ru.hogwarts.school.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.hogwarts.school.model.Student;

import java.util.List;

public interface StudentRepo extends JpaRepository<Student, Long> {
    List<Student> findAllByAgeIs(int age);

    List<Student> findByAgeBetween(int min, int max);

    List<Student> findAllByFacultyId(long facultyId);

}
