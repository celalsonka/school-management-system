-- =============================================================================
-- KAPSAMLı VERİTABANI KISITLAMALARI
-- Veri bütünlüğünü sağlamak için gerekli ve mantıklı tüm kısıtlamaları ekler
-- Hibernate tarafından şema oluşturulduktan sonra çalıştırın
-- =============================================================================

-- =============================================================================
-- 1. SCHOOL (OKUL) TABLOSU KISITLAMALARI
-- =============================================================================

-- Kritik alanlar için NOT NULL kısıtlaması ekle
ALTER TABLE school ALTER COLUMN school_name SET NOT NULL;
ALTER TABLE school ALTER COLUMN address SET NOT NULL;

-- Okul adı için UNIQUE kısıtlaması ekle
ALTER TABLE school ADD CONSTRAINT uq_school_name UNIQUE (school_name);

-- Telefon numarası formatı için CHECK kısıtlaması (Türk telefon formatı)
ALTER TABLE school ADD CONSTRAINT chk_school_phone 
    CHECK (school_phone IS NULL OR school_phone ~ '^[0-9]{10}$');

-- =============================================================================
-- 2. PERSON (KİŞİ) TABLOSU KISITLAMALARI
-- =============================================================================

-- NOT NULL kısıtlamaları ekle
ALTER TABLE person ALTER COLUMN first_name SET NOT NULL;
ALTER TABLE person ALTER COLUMN last_name SET NOT NULL;
ALTER TABLE person ALTER COLUMN school_id SET NOT NULL;

-- TC Kimlik Numarası için UNIQUE kısıtlaması ekle (NULL değerlere izin ver)
-- Partial unique index kullanarak sadece NULL olmayan değerlerin benzersiz olmasını sağla
CREATE UNIQUE INDEX uq_person_tcno ON person (tc_no) WHERE tc_no IS NOT NULL;

-- TC Kimlik Numarası için CHECK kısıtlaması (11 haneli olmalı)
ALTER TABLE person ADD CONSTRAINT chk_person_tcno 
    CHECK (tc_no IS NULL OR (tc_no ~ '^[0-9]{11}$' AND LENGTH(tc_no) = 11));

-- Doğum tarihi için CHECK kısıtlaması (geçmişte olmalı)
ALTER TABLE person ADD CONSTRAINT chk_person_birthdate 
    CHECK (birthdate IS NULL OR birthdate < CURRENT_TIMESTAMP);

-- =============================================================================
-- 3. STUDENT (ÖĞRENCİ) TABLOSU KISITLAMALARI
-- =============================================================================

-- person_id için NOT NULL kısıtlaması ekle
ALTER TABLE student ALTER COLUMN person_id SET NOT NULL;

-- Öğrenci numarası için UNIQUE kısıtlaması ekle
ALTER TABLE student ADD CONSTRAINT uq_student_no UNIQUE (student_no);

-- Öğrenci numarası için CHECK kısıtlaması (pozitif olmalı)
ALTER TABLE student ADD CONSTRAINT chk_student_no 
    CHECK (student_no > 0);

-- =============================================================================
-- 4. TEACHER (ÖĞRETMEN) TABLOSU KISITLAMALARI
-- =============================================================================

-- person_id için NOT NULL kısıtlaması ekle
ALTER TABLE teacher ALTER COLUMN person_id SET NOT NULL;

-- UNIQUE kısıtlamaları zaten entity'de mevcut (sicil_no, mail)
-- İşe giriş tarihi için CHECK kısıtlaması (geçmişte veya bugün olmalı)
ALTER TABLE teacher ADD CONSTRAINT chk_teacher_hiredate 
    CHECK (ise_giris_tarihi <= CURRENT_DATE);

-- E-posta formatı için CHECK kısıtlaması
ALTER TABLE teacher ADD CONSTRAINT chk_teacher_email 
    CHECK (mail ~ '^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,}$');

-- Telefon numarası için CHECK kısıtlaması
ALTER TABLE teacher ADD CONSTRAINT chk_teacher_phone 
    CHECK (tel_no IS NULL OR tel_no ~ '^[0-9]{10}$');

-- =============================================================================
-- 5. CLASSROOM (SINIF) TABLOSU KISITLAMALARI
-- =============================================================================

-- NOT NULL kısıtlamaları ekle
ALTER TABLE classroom ALTER COLUMN capacity SET NOT NULL;
ALTER TABLE classroom ALTER COLUMN school_id SET NOT NULL;

-- Kapasite için CHECK kısıtlaması (pozitif ve makul değer)
ALTER TABLE classroom ADD CONSTRAINT chk_classroom_capacity 
    CHECK (capacity > 0 AND capacity <= 100);

-- Sınıf tipi için CHECK kısıtlaması
ALTER TABLE classroom ADD CONSTRAINT chk_classroom_type 
    CHECK (type IS NULL OR type IN ('Lecture', 'Laboratory', 'Workshop', 'Auditorium', 'Computer Lab', 'Other'));

-- =============================================================================
-- 6. LESSON (DERS) TABLOSU KISITLAMALARI
-- =============================================================================

-- NOT NULL kısıtlamaları ekle
ALTER TABLE lesson ALTER COLUMN lesson_name SET NOT NULL;
ALTER TABLE lesson ALTER COLUMN classroom_code SET NOT NULL;

-- Ders saatleri için CHECK kısıtlaması (pozitif olmalı)
ALTER TABLE lesson ADD CONSTRAINT chk_lesson_hours 
    CHECK (lesson_hours IS NULL OR lesson_hours > 0);

-- =============================================================================
-- 7. EXAM (SINAV) TABLOSU KISITLAMALARI
-- =============================================================================

-- NOT NULL kısıtlamaları ekle
ALTER TABLE exam ALTER COLUMN date SET NOT NULL;
ALTER TABLE exam ALTER COLUMN exam_type SET NOT NULL;
ALTER TABLE exam ALTER COLUMN lesson_id SET NOT NULL;

-- Sınav tarihi için CHECK kısıtlaması (çok eski olmamalı)
ALTER TABLE exam ADD CONSTRAINT chk_exam_date 
    CHECK (date >= '2000-01-01');

-- Sınav tipi için CHECK kısıtlaması
ALTER TABLE exam ADD CONSTRAINT chk_exam_type 
    CHECK (exam_type IN ('Midterm', 'Final', 'Quiz', 'Practical', 'Oral', 'Project'));

-- Haftanın günü için CHECK kısıtlaması
ALTER TABLE exam ADD CONSTRAINT chk_exam_dayofweek 
    CHECK (day_of_week IS NULL OR day_of_week IN ('Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday', 'Sunday'));

-- =============================================================================
-- 8. EXAM_RESULT (SINAV SONUCU) TABLOSU KISITLAMALARI
-- =============================================================================

-- NOT NULL kısıtlamaları ekle
ALTER TABLE exam_result ALTER COLUMN student_id SET NOT NULL;
ALTER TABLE exam_result ALTER COLUMN exam_id SET NOT NULL;
ALTER TABLE exam_result ALTER COLUMN score SET NOT NULL;

-- Not için CHECK kısıtlaması (0-100 arası)
ALTER TABLE exam_result ADD CONSTRAINT chk_examresult_score 
    CHECK (score >= 0 AND score <= 100);

-- Tekrarlanan sınav sonuçlarını önlemek için UNIQUE kısıtlaması
ALTER TABLE exam_result ADD CONSTRAINT uq_exam_result_student_exam 
    UNIQUE (student_id, exam_id);

-- =============================================================================
-- 9. ABSENCE (DEVAMSIZLIK) TABLOSU KISITLAMALARI
-- =============================================================================

-- Tüm NOT NULL kısıtlamaları zaten entity'de mevcut
-- Ders saati için CHECK kısıtlaması (1 ile 12 arası olmalı)
ALTER TABLE absence ADD CONSTRAINT chk_absence_lessonhour 
    CHECK (lesson_hour >= 1 AND lesson_hour <= 12);

-- Devamsızlık tarihi için CHECK kısıtlaması (gelecekte olmamalı)
ALTER TABLE absence ADD CONSTRAINT chk_absence_date 
    CHECK (date <= CURRENT_DATE);

-- Devamsızlık tipi için CHECK kısıtlaması
ALTER TABLE absence ADD CONSTRAINT chk_absence_type 
    CHECK (absence_type IN ('Excused', 'Unexcused', 'Medical', 'Late'));

-- =============================================================================
-- 10. DISCIPLINE_RECORD (DİSİPLİN KAYDI) TABLOSU KISITLAMALARI
-- =============================================================================

-- Tüm NOT NULL kısıtlamaları zaten entity'de mevcut
-- Kayıt tarihi için CHECK kısıtlaması (gelecekte olmamalı)
ALTER TABLE discipline_record ADD CONSTRAINT chk_disciplinerecord_date 
    CHECK (record_date <= CURRENT_DATE);

-- Sebep uzunluğu için CHECK kısıtlaması (en az 5 karakter)
ALTER TABLE discipline_record ADD CONSTRAINT chk_disciplinerecord_reason 
    CHECK (LENGTH(reason) >= 5);

-- Sonuç uzunluğu için CHECK kısıtlaması (en az 5 karakter)
ALTER TABLE discipline_record ADD CONSTRAINT chk_disciplinerecord_result 
    CHECK (LENGTH(result) >= 5);

-- =============================================================================
-- 11. PARENT (VELİ) TABLOSU KISITLAMALARI
-- =============================================================================

-- person_id için NOT NULL kısıtlaması ekle
ALTER TABLE parent ALTER COLUMN person_id SET NOT NULL;

-- Telefon için CHECK kısıtlaması
ALTER TABLE parent ADD CONSTRAINT chk_parent_phone 
    CHECK (telefon IS NULL OR telefon ~ '^[0-9]{10}$');

-- =============================================================================
-- 12. MANAGER (YÖNETİCİ) TABLOSU KISITLAMALARI
-- =============================================================================

-- Tablo mevcutsa person_id için NOT NULL kısıtlaması ekle
DO $$ 
BEGIN
    IF EXISTS (SELECT 1 FROM information_schema.tables WHERE table_name = 'manager') THEN
        ALTER TABLE manager ALTER COLUMN person_id SET NOT NULL;
    END IF;
END $$;

-- =============================================================================
-- 13. COURSE_SCHEDULE (DERS PROGRAMI) TABLOSU KISITLAMALARI
-- =============================================================================

-- Haftanın günü için CHECK kısıtlaması
ALTER TABLE course_schedule ADD CONSTRAINT chk_courseschedule_dayofweek 
    CHECK (day_of_week IN ('Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday', 'Sunday'));

-- Başlangıç ve bitiş saati için CHECK kısıtlaması
ALTER TABLE course_schedule ADD CONSTRAINT chk_courseschedule_hours 
    CHECK (start_hour < end_hour);

-- Çifte rezervasyonu önlemek için UNIQUE kısıtlaması
ALTER TABLE course_schedule ADD CONSTRAINT uq_course_schedule_classroom_time 
    UNIQUE (classroom_code, day_of_week, start_hour);

-- =============================================================================
-- 14. PARENT_STUDENT (VELİ-ÖĞRENCİ) TABLOSU KISITLAMALARI (Bağlantı Tablosu)
-- =============================================================================

-- NOT NULL kısıtlamaları ekle
ALTER TABLE parent_student ALTER COLUMN parent_id SET NOT NULL;
ALTER TABLE parent_student ALTER COLUMN student_id SET NOT NULL;

-- Tekrarlanan ilişkileri önlemek için UNIQUE kısıtlaması
ALTER TABLE parent_student ADD CONSTRAINT uq_parent_student 
    UNIQUE (parent_id, student_id);

-- =============================================================================
-- DOĞRULAMA SORGULARI
-- =============================================================================

-- Tüm kısıtlamaları listele
SELECT 
    tc.table_name,
    tc.constraint_name,
    tc.constraint_type,
    cc.check_clause
FROM information_schema.table_constraints tc
LEFT JOIN information_schema.check_constraints cc 
    ON tc.constraint_name = cc.constraint_name
WHERE tc.table_schema = 'public'
ORDER BY tc.table_name, tc.constraint_type;

-- Kısıtlamaları türe göre say
SELECT 
    constraint_type,
    COUNT(*) as count
FROM information_schema.table_constraints
WHERE table_schema = 'public'
GROUP BY constraint_type
ORDER BY count DESC;

COMMENT ON TABLE school IS 'Benzersiz okul adlarıyla okul bilgilerini saklar';
COMMENT ON TABLE person IS 'Sistemdeki tüm kişiler için benzersiz TC numaraları ile temel tablo';
COMMENT ON TABLE student IS 'Benzersiz öğrenci numaraları ile öğrenci bilgileri';
COMMENT ON TABLE teacher IS 'Benzersiz sicil numaraları ve e-postalar ile öğretmen bilgileri';
COMMENT ON TABLE classroom IS 'Kapasite limitleri (1-100) ile sınıf bilgileri';
COMMENT ON TABLE exam IS 'Tarih ve tür doğrulama ile sınav bilgileri';
COMMENT ON TABLE exam_result IS 'Not doğrulama (0-100) ve benzersiz öğrenci-sınav çiftleri ile sınav sonuçları';
COMMENT ON TABLE absence IS 'Tür ve tarih doğrulama ile devamsızlık kayıtları';
COMMENT ON TABLE discipline_record IS 'Tarih doğrulama ve minimum açıklama uzunluğu ile disiplin kayıtları';

-- =============================================================================
-- KISITLAMALARIN SONU
-- =============================================================================
