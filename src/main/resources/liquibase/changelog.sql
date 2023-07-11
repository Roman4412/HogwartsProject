--liquibase formatted sql

--changeset rpustovalov:2

CREATE INDEX student_n_inx ON student (name);

--changeset rpustovalov:3
CREATE INDEX faculty_nc_inx ON faculty(name,color);


