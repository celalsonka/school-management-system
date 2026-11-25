-- =============================================================================
-- TEST SCRIPT - Find why discipline records aren't being created
-- =============================================================================

-- Step 1: Check if discipline_record table exists
SELECT EXISTS (
   SELECT FROM information_schema.tables 
   WHERE table_name = 'discipline_record'
) as table_exists;

-- Step 2: If table exists, show its structure
SELECT 
    column_name, 
    data_type, 
    is_nullable,
    column_default
FROM information_schema.columns
WHERE table_name = 'discipline_record'
ORDER BY ordinal_position;

-- Step 3: Check for constraints on discipline_record
SELECT
    tc.constraint_name,
    tc.constraint_type,
    kcu.column_name
FROM information_schema.table_constraints tc
JOIN information_schema.key_column_usage kcu 
    ON tc.constraint_name = kcu.constraint_name
WHERE tc.table_name = 'discipline_record';

-- Step 4: Try to manually insert a test discipline record to see the exact error
-- Replace student_id = 1 with an actual student ID from your database
DO $$
DECLARE
    test_student_id BIGINT := 1; -- CHANGE THIS to your test student ID
BEGIN
    -- Try to insert a discipline record
    INSERT INTO discipline_record (student_id, record_date, description, action_taken)
    VALUES (
        test_student_id,
        CURRENT_DATE,
        'Test discipline record',
        'Test action'
    );
    
    RAISE NOTICE 'SUCCESS: Manually inserted discipline record for student %', test_student_id;
    
    -- Clean up the test record
    DELETE FROM discipline_record WHERE description = 'Test discipline record';
    RAISE NOTICE 'Test record cleaned up successfully';
    
EXCEPTION
    WHEN OTHERS THEN
        RAISE NOTICE 'ERROR: Could not insert discipline record';
        RAISE NOTICE 'Error message: %', SQLERRM;
        RAISE NOTICE 'Error detail: %', SQLSTATE;
END $$;

-- Step 5: Check if student IDs in absence table match student table
SELECT 
    a.student_id,
    COUNT(*) as absence_count,
    CASE 
        WHEN s.student_id IS NULL THEN 'STUDENT NOT FOUND'
        ELSE 'Student exists'
    END as student_status
FROM absence a
LEFT JOIN student s ON a.student_id = s.student_id
GROUP BY a.student_id, s.student_id
ORDER BY absence_count DESC
LIMIT 10;

-- Step 6: Show students with 5+ absences
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
