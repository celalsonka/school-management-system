-- Populate Database with Sample Data
-- Run this script in PostgreSQL after the application creates the schema

-- Clear existing data (in correct order due to foreign keys)
DELETE FROM parent_student;
DELETE FROM exam_result;
DELETE FROM course_schedule;
DELETE FROM absence;
DELETE FROM discipline_record;
DELETE FROM exam;
DELETE FROM lesson;
DELETE FROM student;
DELETE FROM teacher;
DELETE FROM parent;
DELETE FROM manager;
DELETE FROM support_staff;
DELETE FROM classroom;
DELETE FROM person;
DELETE FROM school;

-- Reset sequences
ALTER SEQUENCE IF EXISTS person_id_seq RESTART WITH 1;
ALTER SEQUENCE IF EXISTS school_school_id_seq RESTART WITH 1;
ALTER SEQUENCE IF EXISTS lesson_lesson_id_seq RESTART WITH 1;
ALTER SEQUENCE IF EXISTS exam_exam_id_seq RESTART WITH 1;
ALTER SEQUENCE IF EXISTS exam_result_result_id_seq RESTART WITH 1;
ALTER SEQUENCE IF EXISTS absence_absence_id_seq RESTART WITH 1;
ALTER SEQUENCE IF EXISTS discipline_record_record_id_seq RESTART WITH 1;
ALTER SEQUENCE IF EXISTS course_schedule_schedule_id_seq RESTART WITH 1;
ALTER SEQUENCE IF EXISTS parent_student_id_seq RESTART WITH 1;

-- Insert Schools
INSERT INTO school (school_name, address, school_phone, password) VALUES
('Lincoln High School', '123 Main St, Springfield', '555-1000', 'lincoln123'),
('Washington Elementary', '456 Oak Ave, Riverside', '555-2000', 'washington123'),
('Jefferson Middle School', '789 Pine Rd, Lakeside', '555-3000', 'jefferson123');

-- Insert Persons for School 1 (Lincoln High School)
-- Teachers (5)
INSERT INTO person (first_name, last_name, tc_no, birth_date, password, school_id, city, neighborhood, street, apartment, postal_code) VALUES
('John', 'Smith', '12345678901', '1980-05-15', 'teacher123', 1, 'Springfield', 'Downtown', 'Elm St', '10A', '62701'),
('Sarah', 'Johnson', '12345678902', '1985-08-22', 'teacher123', 1, 'Springfield', 'Northside', 'Maple Ave', '5B', '62702'),
('Michael', 'Williams', '12345678903', '1978-03-10', 'teacher123', 1, 'Springfield', 'Westend', 'Oak Dr', '15C', '62703'),
('Emily', 'Brown', '12345678904', '1982-11-30', 'teacher123', 1, 'Springfield', 'Eastside', 'Pine Ln', '20D', '62704'),
('David', 'Jones', '12345678905', '1975-07-18', 'teacher123', 1, 'Springfield', 'Southside', 'Cedar Rd', '8E', '62705');

-- Students (10)
INSERT INTO person (first_name, last_name, tc_no, birth_date, password, school_id, city, neighborhood, street, apartment, postal_code) VALUES
('Alice', 'Anderson', '23456789001', '2008-09-01', 'student123', 1, 'Springfield', 'Downtown', 'Main St', '101', '62701'),
('Bob', 'Baker', '23456789002', '2008-06-15', 'student123', 1, 'Springfield', 'Northside', 'First Ave', '202', '62702'),
('Charlie', 'Clark', '23456789003', '2007-12-20', 'student123', 1, 'Springfield', 'Westend', 'Second St', '303', '62703'),
('Diana', 'Davis', '23456789004', '2008-03-25', 'student123', 1, 'Springfield', 'Eastside', 'Third Rd', '404', '62704'),
('Ethan', 'Evans', '23456789005', '2007-08-10', 'student123', 1, 'Springfield', 'Southside', 'Fourth Ln', '505', '62705'),
('Fiona', 'Foster', '23456789006', '2008-01-05', 'student123', 1, 'Springfield', 'Downtown', 'Fifth Ave', '606', '62701'),
('George', 'Garcia', '23456789007', '2007-10-12', 'student123', 1, 'Springfield', 'Northside', 'Sixth St', '707', '62702'),
('Hannah', 'Harris', '23456789008', '2008-04-28', 'student123', 1, 'Springfield', 'Westend', 'Seventh Dr', '808', '62703'),
('Ian', 'Irwin', '23456789009', '2007-07-14', 'student123', 1, 'Springfield', 'Eastside', 'Eighth Rd', '909', '62704'),
('Julia', 'Jackson', '23456789010', '2008-02-19', 'student123', 1, 'Springfield', 'Southside', 'Ninth Ln', '1010', '62705');

-- Parents (6)
INSERT INTO person (first_name, last_name, tc_no, birth_date, password, school_id, city, neighborhood, street, apartment, postal_code) VALUES
('Robert', 'Anderson', '34567890001', '1980-05-10', 'parent123', 1, 'Springfield', 'Downtown', 'Main St', '101', '62701'),
('Mary', 'Anderson', '34567890002', '1982-08-15', 'parent123', 1, 'Springfield', 'Downtown', 'Main St', '101', '62701'),
('Thomas', 'Baker', '34567890003', '1979-11-22', 'parent123', 1, 'Springfield', 'Northside', 'First Ave', '202', '62702'),
('Jennifer', 'Clark', '34567890004', '1981-02-28', 'parent123', 1, 'Springfield', 'Westend', 'Second St', '303', '62703'),
('James', 'Davis', '34567890005', '1978-06-05', 'parent123', 1, 'Springfield', 'Eastside', 'Third Rd', '404', '62704'),
('Patricia', 'Evans', '34567890006', '1983-09-12', 'parent123', 1, 'Springfield', 'Southside', 'Fourth Ln', '505', '62705');

-- Staff (3)
INSERT INTO person (first_name, last_name, tc_no, birth_date, password, school_id, city, neighborhood, street, apartment, postal_code) VALUES
('William', 'White', '45678900001', '1975-04-20', 'staff123', 1, 'Springfield', 'Downtown', 'Staff Rd', '11A', '62701'),
('Linda', 'Green', '45678900002', '1980-07-25', 'staff123', 1, 'Springfield', 'Northside', 'Admin Ave', '22B', '62702'),
('Richard', 'Black', '45678900003', '1972-10-30', 'staff123', 1, 'Springfield', 'Westend', 'Office St', '33C', '62703');

-- Managers (2)
INSERT INTO person (first_name, last_name, tc_no, birth_date, password, school_id, city, neighborhood, street, apartment, postal_code) VALUES
('Margaret', 'Wilson', '56789000001', '1970-01-15', 'manager123', 1, 'Springfield', 'Downtown', 'Manager Blvd', '1A', '62701'),
('Charles', 'Taylor', '56789000002', '1968-05-20', 'manager123', 1, 'Springfield', 'Northside', 'Executive Dr', '2B', '62702');

-- Insert Teachers
INSERT INTO teacher (person_id, sicil_no, ise_giris_tarihi, mail, tel_no) VALUES
(1, 'T1001', '2015-09-01', 'john.smith@lincoln.edu', '555-1001'),
(2, 'T1002', '2016-08-15', 'sarah.johnson@lincoln.edu', '555-1002'),
(3, 'T1003', '2014-09-01', 'michael.williams@lincoln.edu', '555-1003'),
(4, 'T1004', '2017-08-20', 'emily.brown@lincoln.edu', '555-1004'),
(5, 'T1005', '2013-09-01', 'david.jones@lincoln.edu', '555-1005');

-- Insert Classrooms
INSERT INTO classroom (classroom_code, capacity, type, location, school_id) VALUES
('LC-101', 30, 'Lecture', 'Building A, Floor 1', 1),
('LC-102', 28, 'Lecture', 'Building A, Floor 1', 1),
('LC-201', 32, 'Lecture', 'Building A, Floor 2', 1),
('LAB-301', 25, 'Laboratory', 'Building B, Floor 3', 1),
('LAB-302', 25, 'Laboratory', 'Building B, Floor 3', 1);

-- Insert Students with classroom assignments
INSERT INTO student (person_id, student_no, classroom_code) VALUES
(6, 2024001, 'LC-101'),
(7, 2024002, 'LC-101'),
(8, 2024003, 'LC-102'),
(9, 2024004, 'LC-102'),
(10, 2024005, 'LC-201'),
(11, 2024006, 'LC-201'),
(12, 2024007, 'LAB-301'),
(13, 2024008, 'LAB-301'),
(14, 2024009, 'LAB-302'),
(15, 2024010, 'LAB-302');

-- Insert Parents
INSERT INTO parent (person_id) VALUES
(16), (17), (18), (19), (20), (21);

-- Insert Parent-Student Relationships
INSERT INTO parent_student (parent_id, student_id, relationship_degree) VALUES
-- Alice Anderson (student 1) - parents: Robert (father) and Mary (mother)
(1, 1, 'Father'),
(2, 1, 'Mother'),
-- Bob Baker (student 2) - parent: Thomas (father)
(3, 2, 'Father'),
-- Charlie Clark (student 3) - parent: Jennifer (mother), Robert (uncle)
(4, 3, 'Mother'),
(1, 3, 'Uncle'),
-- Diana Davis (student 4) - parent: James (father)
(5, 4, 'Father'),
-- Ethan Evans (student 5) - parent: Patricia (mother)
(6, 5, 'Mother'),
-- Fiona Foster (student 6) - parents: Thomas (stepfather), Mary (aunt)
(3, 6, 'Stepfather'),
(2, 6, 'Aunt');

-- Insert Support Staff
INSERT INTO support_staff (person_id, sicil_no, ise_giris_tarihi, mail, tel_no, duty) VALUES
(22, 'SS1001', '2010-09-01', 'william.white@lincoln.edu', '555-1011', 'Janitor'),
(23, 'SS1002', '2012-08-15', 'linda.green@lincoln.edu', '555-1012', 'Secretary'),
(24, 'SS1003', '2008-09-01', 'richard.black@lincoln.edu', '555-1013', 'IT Support');

-- Insert Managers
INSERT INTO manager (person_id, sicil_no, ise_giris_tarihi, mail, tel_no, unvan) VALUES
(25, 'M1001', '2005-09-01', 'margaret.wilson@lincoln.edu', '555-1021', 'Principal'),
(26, 'M1002', '2007-08-15', 'charles.taylor@lincoln.edu', '555-1022', 'Vice Principal');

-- Insert Lessons
INSERT INTO lesson (lesson_name, lesson_code, classroom_code) VALUES
('Mathematics 9', 'MATH901', 'LC-101'),
('English Literature', 'ENG901', 'LC-102'),
('Physics', 'PHY901', 'LAB-301'),
('Chemistry', 'CHEM901', 'LAB-302'),
('History', 'HIST901', 'LC-201'),
('Biology', 'BIO901', 'LAB-301');

-- Insert Exams
INSERT INTO exam (lesson_id, date, hour, day_of_week, exam_type) VALUES
(1, '2024-12-15', '09:00', 'Monday', 'Midterm'),
(2, '2024-12-16', '10:00', 'Tuesday', 'Midterm'),
(3, '2024-12-17', '11:00', 'Wednesday', 'Quiz'),
(4, '2024-12-18', '09:00', 'Thursday', 'Midterm'),
(5, '2024-12-19', '14:00', 'Friday', 'Final'),
(6, '2024-12-20', '13:00', 'Saturday', 'Quiz');

-- Insert Exam Results
INSERT INTO exam_result (student_id, exam_id, score) VALUES
-- Mathematics results
(1, 1, 85.5),
(2, 1, 92.0),
(3, 1, 78.5),
(4, 1, 88.0),
-- English results
(3, 2, 91.0),
(4, 2, 87.5),
(5, 2, 79.0),
(6, 2, 94.5),
-- Physics results
(7, 3, 76.0),
(8, 3, 82.5),
-- Chemistry results
(9, 4, 89.0),
(10, 4, 91.5),
-- History results
(5, 5, 84.0),
(6, 5, 90.0),
(1, 5, 87.5),
-- Biology results
(7, 6, 93.0),
(8, 6, 88.5);

-- Insert Course Schedule
INSERT INTO course_schedule (lesson_id, day, lesson_hour) VALUES
-- Monday
(1, 'Monday', '09:00'),
(2, 'Monday', '10:30'),
(3, 'Monday', '13:00'),
-- Tuesday
(4, 'Tuesday', '09:00'),
(5, 'Tuesday', '11:00'),
(6, 'Tuesday', '13:30'),
-- Wednesday
(1, 'Wednesday', '09:00'),
(3, 'Wednesday', '10:30'),
-- Thursday
(2, 'Thursday', '09:00'),
(4, 'Thursday', '11:00'),
-- Friday
(5, 'Friday', '09:00'),
(6, 'Friday', '10:30');

-- Insert Absences
INSERT INTO absence (student_id, date, absence_type, lesson_hour) VALUES
(1, '2024-12-01', 'Excused', 1),
(2, '2024-12-02', 'Unexcused', 2),
(3, '2024-12-03', 'Excused', 3),
(4, '2024-12-05', 'Excused', 1),
(5, '2024-12-06', 'Unexcused', 5),
(7, '2024-12-08', 'Excused', 3),
(8, '2024-12-09', 'Excused', 6),
(9, '2024-12-10', 'Unexcused', 4);

-- Insert Discipline Records
INSERT INTO discipline_record (student_id, record_date, reason, result) VALUES
(2, '2024-11-15', 'Talking during class', 'Verbal warning'),
(3, '2024-11-20', 'Late to class three times', 'Detention'),
(5, '2024-11-22', 'Incomplete homework', 'Parent meeting scheduled'),
(8, '2024-11-25', 'Disruptive behavior', 'Written warning'),
(10, '2024-11-28', 'Using phone in class', 'Phone confiscated, parent notified');

-- Insert data for School 2 (Washington Elementary)
-- Persons for School 2
INSERT INTO person (first_name, last_name, tc_no, birth_date, password, school_id, city, neighborhood, street, apartment, postal_code) VALUES
-- Teachers
('Lisa', 'Martinez', '67890000001', '1984-03-15', 'teacher123', 2, 'Riverside', 'Central', 'Washington Blvd', '50A', '92501'),
('Paul', 'Rodriguez', '67890000002', '1979-07-22', 'teacher123', 2, 'Riverside', 'North', 'Jefferson St', '60B', '92502'),
-- Students
('Kevin', 'Lee', '78900000001', '2012-05-10', 'student123', 2, 'Riverside', 'South', 'Adams Ave', '701', '92503'),
('Laura', 'Kim', '78900000002', '2012-09-15', 'student123', 2, 'Riverside', 'East', 'Madison Dr', '802', '92504'),
('Mason', 'Nguyen', '78900000003', '2011-11-20', 'student123', 2, 'Riverside', 'West', 'Monroe Ln', '903', '92505'),
-- Parents
('Susan', 'Lee', '89000000001', '1985-02-08', 'parent123', 2, 'Riverside', 'South', 'Adams Ave', '701', '92503'),
('Daniel', 'Kim', '89000000002', '1983-06-12', 'parent123', 2, 'Riverside', 'East', 'Madison Dr', '802', '92504'),
-- Manager
('Barbara', 'Lopez', '90000000001', '1972-08-18', 'manager123', 2, 'Riverside', 'Central', 'Principal Way', '1', '92501');

INSERT INTO teacher (person_id, sicil_no, ise_giris_tarihi, mail, tel_no) VALUES
(27, 'T2001', '2018-08-15', 'lisa.martinez@washington.edu', '555-2001'),
(28, 'T2002', '2017-09-01', 'paul.rodriguez@washington.edu', '555-2002');

INSERT INTO classroom (classroom_code, capacity, type, location, school_id) VALUES
('WE-101', 25, 'Lecture', 'Main Building, Room 101', 2),
('WE-102', 25, 'Lecture', 'Main Building, Room 102', 2);

INSERT INTO student (person_id, student_no, classroom_code) VALUES
(29, 2024101, 'WE-101'),
(30, 2024102, 'WE-101'),
(31, 2024103, 'WE-102');

INSERT INTO parent (person_id) VALUES
(32), (33);

INSERT INTO parent_student (parent_id, student_id, relationship_degree) VALUES
(7, 11, 'Mother'),
(8, 12, 'Father');

INSERT INTO manager (person_id, sicil_no, ise_giris_tarihi, mail, tel_no, unvan) VALUES
(34, 'M2001', '2010-07-01', 'barbara.lopez@washington.edu', '555-2021', 'Principal');

-- Insert data for School 3 (Jefferson Middle School)
-- Persons for School 3
INSERT INTO person (first_name, last_name, tc_no, birth_date, password, school_id, city, neighborhood, street, apartment, postal_code) VALUES
-- Teachers
('Nathan', 'Wright', '01000000001', '1981-09-05', 'teacher123', 3, 'Lakeside', 'Downtown', 'Jefferson Rd', '100A', '93001'),
-- Students
('Olivia', 'Scott', '02000000001', '2010-03-12', 'student123', 3, 'Lakeside', 'Hillside', 'Lake View Dr', '1001', '93002'),
('Peter', 'Torres', '02000000002', '2009-08-25', 'student123', 3, 'Lakeside', 'Waterfront', 'Shore Ln', '1102', '93003'),
-- Parent
('Rachel', 'Scott', '03000000001', '1982-11-30', 'parent123', 3, 'Lakeside', 'Hillside', 'Lake View Dr', '1001', '93002');

INSERT INTO teacher (person_id, sicil_no, ise_giris_tarihi, mail, tel_no) VALUES
(35, 'T3001', '2019-08-20', 'nathan.wright@jefferson.edu', '555-3001');

INSERT INTO classroom (classroom_code, capacity, type, location, school_id) VALUES
('JM-201', 28, 'Lecture', 'Building A, Floor 2', 3);

INSERT INTO student (person_id, student_no, classroom_code) VALUES
(36, 2024201, 'JM-201'),
(37, 2024202, 'JM-201');

INSERT INTO parent (person_id) VALUES
(38);

INSERT INTO parent_student (parent_id, student_id, relationship_degree) VALUES
(9, 13, 'Mother');

-- Verification queries
SELECT 'Schools' AS table_name, COUNT(*) AS count FROM school
UNION ALL
SELECT 'Persons', COUNT(*) FROM person
UNION ALL
SELECT 'Teachers', COUNT(*) FROM teacher
UNION ALL
SELECT 'Students', COUNT(*) FROM student
UNION ALL
SELECT 'Parents', COUNT(*) FROM parent
UNION ALL
SELECT 'Parent-Student Relations', COUNT(*) FROM parent_student
UNION ALL
SELECT 'Staff', COUNT(*) FROM support_staff
UNION ALL
SELECT 'Managers', COUNT(*) FROM manager
UNION ALL
SELECT 'Classrooms', COUNT(*) FROM classroom
UNION ALL
SELECT 'Lessons', COUNT(*) FROM lesson
UNION ALL
SELECT 'Exams', COUNT(*) FROM exam
UNION ALL
SELECT 'Exam Results', COUNT(*) FROM exam_result
UNION ALL
SELECT 'Course Schedules', COUNT(*) FROM course_schedule
UNION ALL
SELECT 'Absences', COUNT(*) FROM absence
UNION ALL
SELECT 'Discipline Records', COUNT(*) FROM discipline_record;
