package ru.hogwarts.school.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.hogwarts.school.model.Student;

import java.util.List;

public interface StudentRepo extends JpaRepository<Student, Long> {
    List<Student> findAllByAgeIs(int age);

    List<Student> findByAgeBetween(int min, int max);

    List<Student> findAllByFacultyId(long facultyId);
    @Query(value = "SELECT * FROM student", nativeQuery = true )
    List<Student> getAll();
    @Query(value = "SELECT avg(age) FROM student", nativeQuery = true )
    int getAverageAge();
    @Query(value = "SELECT * FROM student ORDER BY id DESC LIMIT (5)", nativeQuery = true )
    List<Student> getLastFiveStudents();

}
