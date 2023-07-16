ALTER TABLE student
    ADD CONSTRAINT check_age CHECK (age > 15);

ALTER TABLE student
    ADD CONSTRAINT unique_name UNIQUE (name);

ALTER TABLE student
    ALTER COLUMN name SET NOT NULL;

ALTER TABLE faculty
    ADD CONSTRAINT unique_name_faculty UNIQUE (name, color);

ALTER TABLE student
    ALTER COLUMN age SET DEFAULT '20';