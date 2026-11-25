-- =============================================================================
-- DIAGNOSTIC SCRIPT FOR ABSENCE TRIGGER
-- Run this in pgAdmin or psql to diagnose why discipline records aren't being created
-- =============================================================================

-- Step 1: Check if the trigger exists
SELECT 
    trigger_name, 
    event_object_table,
    action_timing,
    action_statement
FROM information_schema.triggers
WHERE trigger_name = 'trg_track_excessive_absences';

-- Step 2: Check if the discipline_record table exists and its structure
SELECT 
    column_name, 
    data_type, 
    is_nullable,
    column_default
FROM information_schema.columns
WHERE table_name = 'discipline_record'
ORDER BY ordinal_position;

-- Step 3: Check absence count for a specific student (replace student_id with your test student)
-- SELECT student_id, COUNT(*) as absence_count
-- FROM absence
-- WHERE student_id = 1  -- Change this to your test student ID
-- GROUP BY student_id;

-- Step 4: Check if any discipline records exist
SELECT COUNT(*) as total_discipline_records
FROM discipline_record;

-- Step 5: View any existing discipline records
SELECT * FROM discipline_record
ORDER BY record_date DESC
LIMIT 10;

-- =============================================================================
-- If the trigger doesn't exist or needs to be updated, run this:
-- =============================================================================

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
    
    RAISE NOTICE 'Student % now has % total absences', NEW.student_id, total_absences;
    
    -- If absences reach or exceed threshold
    IF total_absences >= absence_threshold THEN
        RAISE NOTICE 'Threshold exceeded! Creating discipline record...';
        
        BEGIN
            -- Check if record already exists today
            SELECT COUNT(*) INTO existing_record_count
            FROM discipline_record
            WHERE student_id = NEW.student_id
            AND record_date = CURRENT_DATE
            AND description LIKE 'Excessive absences:%';
            
            IF existing_record_count = 0 THEN
                -- Get student name
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
                
                RAISE NOTICE 'SUCCESS: Discipline record created for student % (%) - % absences', 
                    NEW.student_id, student_full_name, total_absences;
            ELSE
                RAISE NOTICE 'Discipline record already exists for today';
            END IF;
        EXCEPTION
            WHEN OTHERS THEN
                RAISE WARNING 'ERROR creating discipline record: % (SQLSTATE: %)', SQLERRM, SQLSTATE;
        END;
    ELSE
        RAISE NOTICE 'Below threshold: % absences (need %)', total_absences, absence_threshold;
    END IF;
    
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- Drop and recreate the trigger
DROP TRIGGER IF EXISTS trg_track_excessive_absences ON absence;
CREATE TRIGGER trg_track_excessive_absences
    AFTER INSERT ON absence
    FOR EACH ROW
    EXECUTE FUNCTION track_excessive_absences();

-- Verify trigger was created
SELECT 'Trigger created successfully!' as status;

-- =============================================================================
-- TEST: Add an absence to see trigger output
-- =============================================================================
-- Uncomment and run this to test (replace with your student ID):
-- INSERT INTO absence (student_id, absence_date, reason, is_excused)
-- VALUES (1, CURRENT_DATE, 'Test absence for trigger', false);

-- Then check the PostgreSQL logs/messages to see NOTICE and WARNING outputs
