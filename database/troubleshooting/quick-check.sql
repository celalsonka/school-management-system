-- Quick diagnostic: Check if discipline_record table exists and try to insert

-- 1. Does the table exist?
SELECT 
    CASE 
        WHEN EXISTS (SELECT FROM information_schema.tables WHERE table_name = 'discipline_record')
        THEN '✓ TABLE EXISTS'
        ELSE '✗ TABLE DOES NOT EXIST'
    END as table_status;

-- 2. Show table structure (if it exists)
SELECT 
    column_name, 
    data_type,
    is_nullable
FROM information_schema.columns
WHERE table_name = 'discipline_record'
ORDER BY ordinal_position;

-- 3. Count existing discipline records
SELECT COUNT(*) as existing_records FROM discipline_record;

-- 4. Show students with many absences
SELECT 
    s.student_id,
    p.first_name,
    p.last_name,
    COUNT(a.absence_id) as total_absences
FROM student s
JOIN person p ON s.person_id = p.id
LEFT JOIN absence a ON s.student_id = a.student_id
GROUP BY s.student_id, p.first_name, p.last_name
HAVING COUNT(a.absence_id) >= 5
ORDER BY total_absences DESC
LIMIT 5;
