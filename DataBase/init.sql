CREATE SCHEMA IF NOT EXISTS application_schema;

CREATE TABLE IF NOT EXISTS application_schema.person
(
    person_id  BIGSERIAL   not null
        constraint person_pk
            primary key,
    first_name VARCHAR(50) not null,
    last_name  VARCHAR(50) not null,
    birthday   DATE        not null,
    mail       VARCHAR(50) not null,
    gender     VARCHAR(50) not null
);

CREATE INDEX IF NOT EXISTS person_first_name_last_name_index
    on application_schema.person (first_name, last_name);

CREATE TABLE IF NOT EXISTS application_schema."group"
(
    group_id            BIGSERIAL   not null
        constraint group_pk
            primary key,
    group_name          VARCHAR(50) not null,
    course              INTEGER     not null,
    start_date_studying DATE        not null
);

CREATE INDEX IF NOT EXISTS group_group_name_index
    on application_schema."group" (group_name);

CREATE TABLE IF NOT EXISTS application_schema.student
(
    student_id      BIGSERIAL not null
        constraint student_pk
            primary key,
    group_id        BIGINT    not null
        constraint student_group_group_id_fk
            references application_schema."group",
    is_master_group BOOLEAN   not null,
    person_id       BIGINT    not null
        constraint student_person_person_id_fk
            references application_schema.person
);

CREATE TABLE IF NOT EXISTS application_schema.subject
(
    subject_id     BIGSERIAL   not null
        constraint subject_pk
            primary key,
    subject_name   VARCHAR(50) not null,
    lecture_hours  INTEGER     not null,
    practice_hours INTEGER     not null,
    lab_hours      INTEGER     not null
);

CREATE INDEX IF NOT EXISTS subject_subject_name_index
    on application_schema.subject (subject_name);

CREATE TABLE IF NOT EXISTS application_schema.professor
(
    professor_id BIGSERIAL not null
        constraint professor_pk
            primary key,
    person_id    BIGINT    not null
        constraint professor_person_person_id_fk
            references application_schema.person
);


CREATE TABLE IF NOT EXISTS application_schema.subject_and_professor
(
    subject_and_professor_id BIGSERIAL not null
    	constraint subject_and_professor_pk
    	    primary key,
    professor_id BIGINT  not null
        constraint subject_and_professor_professor_professor_id_fk
            references application_schema.professor,
    subject_id   BIGINT  not null
        constraint subject_and_professor_subject_subject_id_fk
            references application_schema.subject,
    seminar      BOOLEAN not null,
    lecture      BOOLEAN not null
);

CREATE TABLE IF NOT EXISTS application_schema.session
(
    session_id BIGSERIAL not null
        constraint session_pk
            primary key,
    date_begin DATE      not null,
    date_end   DATE      not null,
    year       INTEGER   not null
);

CREATE TABLE IF NOT EXISTS application_schema.grade
(
    grade_id       BIGSERIAL   not null
        constraint grade_pk
            primary key,
    professor_id   BIGINT      not null
        constraint grade_professor_professor_id_fk
            references application_schema.professor,
    student_id     BIGINT      not null
        constraint grade_student_student_id_fk
            references application_schema.student,
    subject_id     BIGINT      not null
        constraint grade_subject_subject_id_fk
            references application_schema.subject,
    session_id     BIGINT      not null
        constraint grade_session_session_id_fk
            references application_schema.session,
    reporting_form VARCHAR(50) not null,
    grade          INTEGER     not null
);

\copy application_schema.group(group_name, course, start_date_studying) FROM 'csv/group.csv' DELIMITER ',' CSV
SELECT * FROM application_schema.group;
\copy application_schema.person(first_name, last_name, birthday, mail, gender) FROM 'csv/person.csv' DELIMITER ',' CSV
\copy application_schema.professor(person_id) FROM 'csv/professor.csv' DELIMITER ',' CSV
\copy application_schema.session(date_begin, date_end, year) FROM 'csv/session.csv' DELIMITER ',' CSV
\copy application_schema.student(group_id, is_master_group, person_id) FROM 'csv/student.csv' DELIMITER ',' CSV
\copy application_schema.subject(subject_name, lecture_hours, practice_hours, lab_hours) FROM 'csv/subject.csv' DELIMITER ',' CSV
\copy application_schema.subject_and_professor(professor_id, subject_id, seminar, lecture) FROM 'csv/subject_and_professor.csv' DELIMITER ',' CSV
\copy application_schema.grade(professor_id, student_id, subject_id, session_id, reporting_form, grade)  FROM 'csv/grade.csv' DELIMITER ',' CSV HEADER
