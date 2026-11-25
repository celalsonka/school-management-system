
-- Drop existing function/procedure
DROP FUNCTION IF EXISTS get_classroom_averages();
DROP PROCEDURE IF EXISTS calculate_classroom_averages();
DROP PROCEDURE IF EXISTS generate_classroom_report();

-- Create a stored PROCEDURE using CURSOR for reporting
-- This demonstrates cursor usage for row-by-row processing
CREATE OR REPLACE PROCEDURE generate_classroom_report()
LANGUAGE plpgsql
AS $$
DECLARE
    -- Declare cursor for classroom data
    classroom_cursor CURSOR FOR
        SELECT 
            c.classroom_code,
            c.capacity,
            COUNT(DISTINCT s.student_id) AS student_count,
            AVG(er.score) AS avg_score,
            COUNT(er.result_id) AS exam_count
        FROM classroom c
        LEFT JOIN student s ON s.classroom_code = c.classroom_code
        LEFT JOIN exam_result er ON er.student_id = s.student_id
        GROUP BY c.classroom_code, c.capacity
        ORDER BY c.classroom_code;
    
    -- Variables to hold cursor data
    v_classroom_code VARCHAR(50);
    v_capacity INT;
    v_student_count BIGINT;
    v_avg_score NUMERIC;
    v_exam_count BIGINT;
    v_performance_status VARCHAR(30);
BEGIN
    -- Drop and create report table
    DROP TABLE IF EXISTS classroom_report;
    CREATE TABLE classroom_report (
        classroom_code VARCHAR(50),
        capacity INT,
        student_count BIGINT,
        average_score NUMERIC(5,2),
        exam_count BIGINT,
        performance_status VARCHAR(30),
        capacity_usage NUMERIC(5,2)
    );
    
    -- Open cursor
    OPEN classroom_cursor;
    
    -- Loop through cursor
    LOOP
        -- Fetch next row
        FETCH classroom_cursor INTO 
            v_classroom_code, 
            v_capacity, 
            v_student_count, 
            v_avg_score, 
            v_exam_count;
        
        -- Exit loop when no more rows
        EXIT WHEN NOT FOUND;
        
        -- Determine performance status based on average
        IF v_avg_score IS NULL THEN
            v_performance_status := 'No Data';
            v_avg_score := 0.00;
        ELSIF v_avg_score >= 85 THEN
            v_performance_status := 'Excellent';
        ELSIF v_avg_score >= 70 THEN
            v_performance_status := 'Good';
        ELSIF v_avg_score >= 50 THEN
            v_performance_status := 'Average';
        ELSE
            v_performance_status := 'Needs Improvement';
        END IF;
        
        -- Insert processed data into report table
        INSERT INTO classroom_report VALUES (
            v_classroom_code,
            v_capacity,
            v_student_count,
            ROUND(v_avg_score, 2),
            v_exam_count,
            v_performance_status,
            CASE 
                WHEN v_capacity > 0 THEN ROUND((v_student_count::NUMERIC / v_capacity) * 100, 2)
                ELSE 0.00
            END
        );
    END LOOP;
    
    -- Close cursor
    CLOSE classroom_cursor;
    
    RAISE NOTICE 'Classroom report generated successfully using CURSOR';
END;
$$;

-- Create a stored PROCEDURE (not function)
-- This procedure creates a temporary table with classroom averages
CREATE OR REPLACE PROCEDURE calculate_classroom_averages()
LANGUAGE plpgsql
AS $$
BEGIN
    -- Drop temporary table if exists
    DROP TABLE IF EXISTS temp_classroom_averages;
    
    -- Create temporary table with results
    CREATE TEMP TABLE temp_classroom_averages AS
    SELECT 
        c.classroom_code::VARCHAR(50) AS classroom_code,
        CASE 
            WHEN AVG(er.score) IS NULL THEN 0.00::NUMERIC(5,2)
            ELSE ROUND(AVG(er.score)::NUMERIC, 2)::NUMERIC(5,2)
        END AS average_score,
        COUNT(DISTINCT s.student_id)::BIGINT AS total_students,
        COUNT(er.result_id)::BIGINT AS total_exams
    FROM 
        classroom c
    LEFT JOIN 
        student s ON s.classroom_code = c.classroom_code
    LEFT JOIN 
        exam_result er ON er.student_id = s.student_id
    GROUP BY 
        c.classroom_code
    ORDER BY 
        c.classroom_code;
        
    RAISE NOTICE 'Classroom averages calculated and stored in temp_classroom_averages table';
END;
$$;

-- Create a FUNCTION wrapper to use with the web interface
-- This allows SELECT * FROM get_classroom_averages() to work
CREATE OR REPLACE FUNCTION get_classroom_averages()
RETURNS TABLE (
    classroom_code VARCHAR(50),
    average_score NUMERIC(5,2),
    total_students BIGINT,
    total_exams BIGINT
) 
LANGUAGE plpgsql
AS $$
BEGIN
    -- Call the stored procedure
    CALL calculate_classroom_averages();
    
    -- Return results from temporary table
    RETURN QUERY
    SELECT * FROM temp_classroom_averages;
END;
$$;

-- Usage examples:
-- To generate report using CURSOR:
-- CALL generate_classroom_report();
-- SELECT * FROM classroom_report;

-- To call the stored procedure directly:
-- CALL calculate_classroom_averages();
-- SELECT * FROM temp_classroom_averages;

-- To use via function (for web interface):
-- SELECT * FROM get_classroom_averages();


