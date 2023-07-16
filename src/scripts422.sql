CREATE TABLE cars
(
    car_id SERIAL PRIMARY KEY,
    brand  TEXT UNIQUE NOT NULL,
    model  TEXT UNIQUE NOT NULL,
    price  money       NOT NULL
);

CREATE TABLE persons
(
    person_id      SERIAL PRIMARY KEY,
    name           TEXT    NOT NULL,
    age            INTEGER NOT NULL,
    driver_license BOOLEAN NOT NULL,
    car_id         SERIAL REFERENCES cars (car_id)
);

SELECT student.name, student.age, faculty.name
FROM student
         INNER JOIN faculty ON student.faculty_id = faculty.id;

SELECT student.name, student.age, avatar.file_path
FROM student
         INNER JOIN avatar ON student.id = avatar.student_id;