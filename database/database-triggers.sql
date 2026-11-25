-- =============================================================================
-- DATABASE TRIGGERS FOR SCHOOL MANAGEMENT SYSTEM
-- =============================================================================
-- These triggers ensure data integrity, automate calculations, and maintain audit trails
-- Run these triggers in PostgreSQL after creating the database schema
-- =============================================================================

-- =============================================================================
-- TRIGGER 1: Auto-calculate and validate classroom capacity
-- Purpose: Prevents overbooking of classrooms by checking student count
-- =============================================================================

-- First, create a function to check classroom capacity
CREATE OR REPLACE FUNCTION check_classroom_capacity()
RETURNS TRIGGER AS $$
DECLARE
    current_capacity INTEGER;
    student_count INTEGER;
    classroom_capacity INTEGER;
BEGIN
    -- Get the classroom capacity
    SELECT capacity INTO classroom_capacity
    FROM classroom
    WHERE classroom_code = NEW.classroom_code;
    
    -- Count current students in this classroom
    SELECT COUNT(*) INTO student_count
    FROM student
    WHERE classroom_code = NEW.classroom_code;
    
    -- Check if adding this student would exceed capacity
    IF student_count >= classroom_capacity THEN
        RAISE EXCEPTION 'Classroom capacity exceeded! Classroom % has capacity of %, but already has % students.',
            NEW.classroom_code, classroom_capacity, student_count;
    END IF;
    
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- Create the trigger
DROP TRIGGER IF EXISTS trg_check_classroom_capacity ON student;
CREATE TRIGGER trg_check_classroom_capacity
    BEFORE INSERT OR UPDATE OF classroom_code ON student
    FOR EACH ROW
    EXECUTE FUNCTION check_classroom_capacity();

-- =============================================================================
-- TRIGGER 2: Automatic absence tracking and discipline action
-- Purpose: Automatically creates a discipline record when a student has too many absences
-- =============================================================================

CREATE OR REPLACE FUNCTION track_excessive_absences()
RETURNS TRIGGER AS $$
DECLARE
    total_absences INTEGER;
    absence_threshold INTEGER := 5; -- Threshold for disciplinary action
    student_full_name TEXT;
    existing_record_count INTEGER;
BEGIN
    -- Count total absences for this student (including the new one being inserted)
    SELECT COUNT(*) INTO total_absences
    FROM absence
    WHERE student_id = NEW.student_id;
    
    -- If absences reach or exceed threshold, create a discipline record
    IF total_absences >= absence_threshold THEN
        BEGIN
            -- Check if a discipline record for excessive absences already exists today
            SELECT COUNT(*) INTO existing_record_count
            FROM discipline_record
            WHERE student_id = NEW.student_id
            AND record_date = CURRENT_DATE
            AND reason LIKE 'Excessive absences%';
            
            -- Only create a new record if one doesn't exist today
            IF existing_record_count = 0 THEN
                -- Get student name for the result message
                SELECT p.first_name || ' ' || p.last_name INTO student_full_name
                FROM student s
                JOIN person p ON s.person_id = p.id
                WHERE s.student_id = NEW.student_id;
                
                -- Insert discipline record with CORRECT column names (reason and result)
                INSERT INTO discipline_record (student_id, record_date, reason, result)
                VALUES (
                    NEW.student_id,
                    CURRENT_DATE,
                    'Excessive absences: ' || total_absences || ' total absences recorded',
                    'Warning letter sent to parents. Student ' || student_full_name || ' scheduled for counselor meeting.'
                );
                
                -- Log the action
                RAISE NOTICE 'Discipline record created for student % due to % absences', 
                    student_full_name, total_absences;
            ELSE
                RAISE NOTICE 'Discipline record already exists for student today';
            END IF;
        EXCEPTION
            WHEN OTHERS THEN
                -- Log the error but don't prevent the absence from being added
                RAISE WARNING 'Could not create discipline record: %', SQLERRM;
        END;
    END IF;
    
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- Create the trigger
DROP TRIGGER IF EXISTS trg_track_excessive_absences ON absence;
CREATE TRIGGER trg_track_excessive_absences
    AFTER INSERT ON absence
    FOR EACH ROW
    EXECUTE FUNCTION track_excessive_absences();

-- =============================================================================
-- TRIGGER 3: Validate exam results score range
-- Purpose: Ensures exam scores are within valid range (0-100)
-- =============================================================================

CREATE OR REPLACE FUNCTION validate_exam_score()
RETURNS TRIGGER AS $$
BEGIN
    -- Check if score is within valid range
    IF NEW.score < 0 OR NEW.score > 100 THEN
        RAISE EXCEPTION 'Invalid exam score! Score must be between 0 and 100. Received: %', NEW.score;
    END IF;
    
    -- Check if score is null
    IF NEW.score IS NULL THEN
        RAISE EXCEPTION 'Exam score cannot be null!';
    END IF;
    
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- Create the trigger
DROP TRIGGER IF EXISTS trg_validate_exam_score ON exam_result;
CREATE TRIGGER trg_validate_exam_score
    BEFORE INSERT OR UPDATE OF score ON exam_result
    FOR EACH ROW
    EXECUTE FUNCTION validate_exam_score();

-- =============================================================================
-- TRIGGER 4: Audit log for student grade changes
-- Purpose: Creates an audit trail when exam results are modified
-- =============================================================================

-- First, create the audit table if it doesn't exist
CREATE TABLE IF NOT EXISTS exam_result_audit (
    audit_id SERIAL PRIMARY KEY,
    result_id INTEGER,
    student_id BIGINT,
    exam_id INTEGER,
    old_score INTEGER,
    new_score INTEGER,
    changed_by VARCHAR(100),
    change_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    operation VARCHAR(10)
);

CREATE OR REPLACE FUNCTION audit_exam_result_changes()
RETURNS TRIGGER AS $$
BEGIN
    IF TG_OP = 'UPDATE' THEN
        -- Log the old and new scores
        INSERT INTO exam_result_audit (result_id, student_id, exam_id, old_score, new_score, changed_by, operation)
        VALUES (OLD.result_id, OLD.student_id, OLD.exam_id, OLD.score, NEW.score, current_user, 'UPDATE');
        
        RAISE NOTICE 'Exam result updated: Student %, Exam %, Old score: %, New score: %',
            NEW.student_id, NEW.exam_id, OLD.score, NEW.score;
    ELSIF TG_OP = 'DELETE' THEN
        -- Log deletion
        INSERT INTO exam_result_audit (result_id, student_id, exam_id, old_score, changed_by, operation)
        VALUES (OLD.result_id, OLD.student_id, OLD.exam_id, OLD.score, current_user, 'DELETE');
        
        RETURN OLD;
    END IF;
    
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- Create the trigger
DROP TRIGGER IF EXISTS trg_audit_exam_result_changes ON exam_result;
CREATE TRIGGER trg_audit_exam_result_changes
    AFTER UPDATE OR DELETE ON exam_result
    FOR EACH ROW
    EXECUTE FUNCTION audit_exam_result_changes();

-- =============================================================================
-- TRIGGER 5: Prevent deletion of teachers with assigned lessons
-- Purpose: Maintains data integrity by preventing deletion of teachers who have lessons
-- =============================================================================

CREATE OR REPLACE FUNCTION prevent_teacher_deletion_with_lessons()
RETURNS TRIGGER AS $$
DECLARE
    lesson_count INTEGER;
    teacher_name TEXT;
BEGIN
    -- Count lessons assigned to this teacher
    SELECT COUNT(*) INTO lesson_count
    FROM teacher_lesson
    WHERE teacher_id = OLD.id;
    
    -- If teacher has lessons, prevent deletion
    IF lesson_count > 0 THEN
        -- Get teacher name
        SELECT p.first_name || ' ' || p.last_name INTO teacher_name
        FROM person p
        WHERE p.id = OLD.person_id;
        
        RAISE EXCEPTION 'Cannot delete teacher %! Teacher has % lesson(s) assigned. Please reassign lessons first.',
            teacher_name, lesson_count;
    END IF;
    
    RETURN OLD;
END;
$$ LANGUAGE plpgsql;

-- Create the trigger
DROP TRIGGER IF EXISTS trg_prevent_teacher_deletion ON teacher;
CREATE TRIGGER trg_prevent_teacher_deletion
    BEFORE DELETE ON teacher
    FOR EACH ROW
    EXECUTE FUNCTION prevent_teacher_deletion_with_lessons();

-- =============================================================================
-- TRIGGER 6: Auto-update timestamp for person records
-- Purpose: Automatically tracks when person records are modified
-- =============================================================================

-- First, add a last_updated column to person table if it doesn't exist
DO $$ 
BEGIN
    IF NOT EXISTS (SELECT 1 FROM information_schema.columns 
                   WHERE table_name='person' AND column_name='last_updated') THEN
        ALTER TABLE person ADD COLUMN last_updated TIMESTAMP DEFAULT CURRENT_TIMESTAMP;
    END IF;
END $$;

CREATE OR REPLACE FUNCTION update_person_timestamp()
RETURNS TRIGGER AS $$
BEGIN
    NEW.last_updated = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- Create the trigger
DROP TRIGGER IF EXISTS trg_update_person_timestamp ON person;
CREATE TRIGGER trg_update_person_timestamp
    BEFORE UPDATE ON person
    FOR EACH ROW
    EXECUTE FUNCTION update_person_timestamp();

-- =============================================================================
-- TRIGGER 7: Validate teacher assignment to lessons
-- Purpose: Ensures only teachers from the same school are assigned to lessons
-- =============================================================================

CREATE OR REPLACE FUNCTION validate_teacher_lesson_school()
RETURNS TRIGGER AS $$
DECLARE
    teacher_school_id INTEGER;
    lesson_school_id INTEGER;
BEGIN
    -- Get teacher's school
    SELECT p.school_id INTO teacher_school_id
    FROM teacher t
    JOIN person p ON t.person_id = p.id
    WHERE t.id = NEW.teacher_id;
    
    -- Get lesson's school (through classroom)
    SELECT c.school_id INTO lesson_school_id
    FROM lesson l
    JOIN classroom c ON l.classroom_code = c.classroom_code
    WHERE l.lesson_id = NEW.lesson_id;
    
    -- Validate they match
    IF teacher_school_id != lesson_school_id THEN
        RAISE EXCEPTION 'School mismatch! Teacher belongs to school %, but lesson belongs to school %. Teachers can only be assigned to lessons in their own school.',
            teacher_school_id, lesson_school_id;
    END IF;
    
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- Create the trigger
DROP TRIGGER IF EXISTS trg_validate_teacher_lesson_school ON teacher_lesson;
CREATE TRIGGER trg_validate_teacher_lesson_school
    BEFORE INSERT OR UPDATE ON teacher_lesson
    FOR EACH ROW
    EXECUTE FUNCTION validate_teacher_lesson_school();

-- =============================================================================
-- VERIFICATION QUERIES
-- Run these to verify triggers are created successfully
-- =============================================================================

-- List all triggers
SELECT 
    trigger_name, 
    event_manipulation, 
    event_object_table,
    action_timing
FROM information_schema.triggers
WHERE trigger_schema = 'public'
ORDER BY event_object_table, trigger_name;

-- List all functions related to triggers
SELECT 
    routine_name,
    routine_type,
    data_type
FROM information_schema.routines
WHERE routine_schema = 'public'
AND routine_type = 'FUNCTION'
AND routine_name LIKE '%classroom%' 
   OR routine_name LIKE '%absence%'
   OR routine_name LIKE '%exam%'
   OR routine_name LIKE '%teacher%'
   OR routine_name LIKE '%person%'
ORDER BY routine_name;

-- =============================================================================
-- TEST SCRIPTS
-- Use these to test the triggers
-- =============================================================================

-- TEST 1: Test classroom capacity trigger
-- This should fail if classroom is at capacity
-- INSERT INTO student (student_no, person_id, classroom_code) 
-- VALUES ('TEST001', 1, 'A101');

-- TEST 2: Test exam score validation
-- This should fail (score > 100)
-- INSERT INTO exam_result (student_id, exam_id, score) 
-- VALUES (1, 1, 150);

-- TEST 3: View audit log
-- SELECT * FROM exam_result_audit ORDER BY change_date DESC;

-- TEST 4: Test absence tracking
-- Insert multiple absences to trigger discipline record
-- INSERT INTO absence (student_id, absence_date, reason) 
-- VALUES (1, CURRENT_DATE, 'Test absence');

COMMENT ON FUNCTION check_classroom_capacity() IS 'Trigger function to validate classroom capacity is not exceeded';
COMMENT ON FUNCTION track_excessive_absences() IS 'Trigger function to automatically create discipline records for excessive absences';
COMMENT ON FUNCTION validate_exam_score() IS 'Trigger function to validate exam scores are between 0 and 100';
COMMENT ON FUNCTION audit_exam_result_changes() IS 'Trigger function to maintain audit trail for exam result changes';
COMMENT ON FUNCTION prevent_teacher_deletion_with_lessons() IS 'Trigger function to prevent deletion of teachers with assigned lessons';
COMMENT ON FUNCTION update_person_timestamp() IS 'Trigger function to automatically update last_updated timestamp';
COMMENT ON FUNCTION validate_teacher_lesson_school() IS 'Trigger function to ensure teachers are only assigned to lessons in their school';

-- =============================================================================
-- END OF TRIGGER DEFINITIONS
-- =============================================================================
