package ru.hogwarts.school.dto;

import ru.hogwarts.school.model.Avatar;

public class StudentDtoOut {
    private long id;
    private String name;
    private int age;
    private FacultyDtoOut faculty;
    private Avatar avatar;

    public StudentDtoOut(long id, String name, int age, FacultyDtoOut faculty,  Avatar avatar) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.faculty = faculty;
        this.avatar = avatar;
    }

    public StudentDtoOut() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public FacultyDtoOut getFaculty() {
        return faculty;
    }

    public void setFaculty(FacultyDtoOut faculty) {
        this.faculty = faculty;
    }

    public Avatar getAvatar() {
        return avatar;
    }

    public void setAvatar(Avatar avatar) {
        this.avatar = avatar;
    }
}
