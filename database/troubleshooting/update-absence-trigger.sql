-- Update the absence tracking trigger
-- Run this in PostgreSQL to fix the excessive absence tracking

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
            AND description LIKE 'Excessive absences:%';
            
            -- Only create a new record if one doesn't exist today
            IF existing_record_count = 0 THEN
                -- Get student name for the description
                SELECT p.first_name || ' ' || p.last_name INTO student_full_name
                FROM student s
                JOIN person p ON s.person_id = p.id
                WHERE s.student_id = NEW.student_id;
                
                -- Insert discipline record
                INSERT INTO discipline_record (student_id, record_date, description, action_taken)
                VALUES (
                    NEW.student_id,
                    CURRENT_DATE,
                    'Excessive absences: ' || total_absences || ' total absences recorded.',
                    'Warning letter sent to parents. Meeting with counselor scheduled.'
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

-- Test the trigger
-- SELECT * FROM absence WHERE student_id = 1; -- Check current absences
-- SELECT * FROM discipline_record WHERE student_id = 1; -- Check discipline records
