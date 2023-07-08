package ru.hogwarts.school.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.hogwarts.school.model.Avatar;

public interface AvatarRepo extends JpaRepository<Avatar,Integer> {

        Optional<Avatar> findByStudent_Id(long studentId);

        @Query(value = "select * from avatar ", nativeQuery = true)
        List<Avatar> getAll(PageRequest pageRequest);

}