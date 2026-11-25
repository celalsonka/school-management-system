-- =============================================================================
-- FIXED ABSENCE TRACKING TRIGGER
-- This trigger creates discipline records when students have 5+ absences
-- =============================================================================

-- Drop existing trigger and function
DROP TRIGGER IF EXISTS trg_track_excessive_absences ON absence;
DROP FUNCTION IF EXISTS track_excessive_absences();

-- Create the corrected function
CREATE OR REPLACE FUNCTION track_excessive_absences()
RETURNS TRIGGER AS $$
DECLARE
    total_absences INTEGER;
    absence_threshold INTEGER := 5;
    student_full_name TEXT;
    existing_record_count INTEGER;
BEGIN
    -- Count total absences for this student
    SELECT COUNT(*) INTO total_absences
    FROM absence
    WHERE student_id = NEW.student_id;
    
    -- If absences reach or exceed threshold
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
                
                -- Insert discipline record with CORRECT column names
                INSERT INTO discipline_record (student_id, record_date, reason, result)
                VALUES (
                    NEW.student_id,
                    CURRENT_DATE,
                    'Excessive absences: ' || total_absences || ' total absences recorded',
                    'Warning letter sent to parents. Student ' || student_full_name || ' scheduled for counselor meeting.'
                );
                
                RAISE NOTICE 'Discipline record created for student % due to % absences', 
                    student_full_name, total_absences;
            END IF;
        EXCEPTION
            WHEN OTHERS THEN
                -- Log error but don't block the absence insertion
                RAISE WARNING 'Could not create discipline record: %', SQLERRM;
        END;
    END IF;
    
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- Create the trigger
CREATE TRIGGER trg_track_excessive_absences
    AFTER INSERT ON absence
    FOR EACH ROW
    EXECUTE FUNCTION track_excessive_absences();

-- Verify it was created
SELECT 'Trigger recreated successfully with correct column names!' as status;

-- Test query to see students with 5+ absences
SELECT 
    s.student_id,
    p.first_name || ' ' || p.last_name as student_name,
    COUNT(a.absence_id) as total_absences
FROM student s
JOIN person p ON s.person_id = p.id
LEFT JOIN absence a ON s.student_id = a.student_id
GROUP BY s.student_id, p.first_name, p.last_name
HAVING COUNT(a.absence_id) >= 5
ORDER BY total_absences DESC;

-- Check any existing discipline records
SELECT 
    dr.discipline_record_id,
    dr.student_id,
    p.first_name || ' ' || p.last_name as student_name,
    dr.record_date,
    dr.reason,
    dr.result
FROM discipline_record dr
JOIN student s ON dr.student_id = s.student_id
JOIN person p ON s.person_id = p.id
ORDER BY dr.record_date DESC;
