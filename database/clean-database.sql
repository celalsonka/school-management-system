-- Clean all data from database
-- Run this before starting the application with fresh schema

-- Drop all tables in correct order (respecting foreign keys)
DROP TABLE IF EXISTS parent_student CASCADE;
DROP TABLE IF EXISTS exam_result CASCADE;
DROP TABLE IF EXISTS course_schedule CASCADE;
DROP TABLE IF EXISTS absence CASCADE;
DROP TABLE IF EXISTS discipline_record CASCADE;
DROP TABLE IF EXISTS exam CASCADE;
DROP TABLE IF EXISTS lesson CASCADE;
DROP TABLE IF EXISTS teacher_lesson CASCADE;
DROP TABLE IF EXISTS teacher CASCADE;
DROP TABLE IF EXISTS student CASCADE;
DROP TABLE IF EXISTS parent CASCADE;
DROP TABLE IF EXISTS manager CASCADE;
DROP TABLE IF EXISTS support_staff CASCADE;
DROP TABLE IF EXISTS message CASCADE;
DROP TABLE IF EXISTS conversation CASCADE;
DROP TABLE IF EXISTS classroom CASCADE;
DROP TABLE IF EXISTS _class CASCADE;
DROP TABLE IF EXISTS person CASCADE;
DROP TABLE IF EXISTS school CASCADE;

-- All tables dropped - Hibernate will recreate them on next application start
