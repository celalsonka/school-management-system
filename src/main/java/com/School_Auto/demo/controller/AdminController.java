package com.School_Auto.demo.controller;

import com.School_Auto.demo.entity.Absence;
import com.School_Auto.demo.entity.Classroom;
import com.School_Auto.demo.entity.CourseSchedule;
import com.School_Auto.demo.entity.DisciplineRecord;
import com.School_Auto.demo.entity.Exam;
import com.School_Auto.demo.entity.ExamResult;
import com.School_Auto.demo.entity.Lesson;
import com.School_Auto.demo.entity.Manager;
import com.School_Auto.demo.entity.Parent;
import com.School_Auto.demo.entity.ParentStudent;
import com.School_Auto.demo.entity.Person;
import com.School_Auto.demo.entity.School;
import com.School_Auto.demo.entity.Staff;
import com.School_Auto.demo.entity.Student;
import com.School_Auto.demo.entity.Teacher;
import com.School_Auto.demo.security.Passwords;
import com.School_Auto.demo.service.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Set;
import java.util.HashSet;
import java.util.ArrayList;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final AbsenceService absenceService;
    private final ClassroomService classroomService;
    private final CourseScheduleService courseScheduleService;
    private final DisciplineRecordService disciplineRecordService;
    private final ExamService examService;
    private final ExamResultService examResultService;
    private final LessonService lessonService;
    private final ManagerService managerService;
    private final ParentService parentService;
    private final SchoolService schoolService;
    private final StaffService staffService;
    private final StudentService studentService;
    private final TeacherService teacherService;
    private final ParentStudentService parentStudentService;

    // Eklediğiniz servisler
    private final PersonService personService;

    // The logged-in school is stored per user in the HTTP session. (A controller is a singleton shared by
    // all users, so keeping it in a field would log every visitor in as the last school that signed in.)
    private static final String SCHOOL_SESSION_KEY = "authenticatedSchoolId";
    private final HttpSession session;

    private Integer currentSchoolId() {
        return (Integer) session.getAttribute(SCHOOL_SESSION_KEY);
    }

    // Helper method to check authentication
    private boolean isAuthenticated() {
        return currentSchoolId() != null;
    }

    public AdminController(AbsenceService absenceService,
                           ClassroomService classroomService,
                           CourseScheduleService courseScheduleService,
                           DisciplineRecordService disciplineRecordService,
                           ExamService examService,
                           ExamResultService examResultService,
                           LessonService lessonService,
                           ManagerService managerService,
                           ParentService parentService,
                           SchoolService schoolService,
                           StaffService staffService,
                           StudentService studentService,
                           TeacherService teacherService,
                           ParentStudentService parentStudentService,
                           PersonService personService,
                           HttpSession session) {
        this.session = session; // request-scoped proxy: always resolves to the current user's session
        this.absenceService = absenceService;
        this.classroomService = classroomService;
        this.courseScheduleService = courseScheduleService;
        this.disciplineRecordService = disciplineRecordService;
        this.examService = examService;
        this.examResultService = examResultService;
        this.lessonService = lessonService;
        this.managerService = managerService;
        this.parentService = parentService;
        this.schoolService = schoolService;
        this.staffService = staffService;
        this.studentService = studentService;
        this.teacherService = teacherService;
        this.parentStudentService = parentStudentService;
        this.personService = personService;
    }

    // ==================== SCHOOL AUTHENTICATION ====================
    @GetMapping("/school-login")
    public String showSchoolLogin(Model model) {
        List<School> schools = schoolService.findAll();
        model.addAttribute("schools", schools);
        return "admin/school-login";
    }

    @PostMapping("/school-authenticate")
    public String authenticateSchool(@RequestParam("schoolId") int schoolId,
                                     @RequestParam("password") String password,
                                     Model model) {
        School school = schoolService.findById(schoolId).orElse(null);

        if (school != null && Passwords.matches(password, school.getPassword())) {
            if (Passwords.needsUpgrade(school.getPassword())) {
                // Legacy plain-text password: replace it with a BCrypt hash now that we know it.
                school.setPassword(Passwords.hash(password));
                schoolService.save(school);
            }
            session.setAttribute(SCHOOL_SESSION_KEY, schoolId);
            return "redirect:/admin/showCatalog";
        } else {
            model.addAttribute("error", "Invalid password for selected school");
            List<School> schools = schoolService.findAll();
            model.addAttribute("schools", schools);
            return "admin/school-login";
        }
    }

    @GetMapping("/logout")
    public String logout() {
        session.invalidate();
        return "redirect:/admin/school-login";
    }

    // Root admin URL redirects to school login
    @GetMapping("")
    public String adminRoot() {
        if (currentSchoolId() == null) {
            return "redirect:/admin/school-login";
        }
        return "redirect:/admin/dashboard";
    }

    // ==================== COURSE SCHEDULE MANAGEMENT ====================
    @GetMapping("/classroom/{classroomCode}/courseschedule")
    public String showCourseScheduleForm(@PathVariable("classroomCode") String classroomCode, Model model) {
        List<CourseSchedule> schedules = courseScheduleService.findByClassroomCode(classroomCode);
        List<Lesson> lessons = lessonService.findAll().stream()
            .filter(l -> l.getClassroom() != null && l.getClassroom().getSchool() != null 
                && l.getClassroom().getSchool().getSchoolId() == currentSchoolId())
            .collect(java.util.stream.Collectors.toList());
        List<Exam> exams = examService.findAll().stream()
            .filter(e -> e.getLesson() != null && e.getLesson().getClassroom() != null 
                && e.getLesson().getClassroom().getSchool() != null
                && e.getLesson().getClassroom().getSchool().getSchoolId() == currentSchoolId())
            .collect(java.util.stream.Collectors.toList());
        model.addAttribute("classroomCode", classroomCode);
        model.addAttribute("schedules", schedules);
        model.addAttribute("lessons", lessons);
        model.addAttribute("exams", exams);
        return "admin/courseSchedule-form";
    }

    @PostMapping("/classroom/{classroomCode}/courseschedule")
    public String addCourseSchedule(@PathVariable("classroomCode") String classroomCode,
                                    @RequestParam("day") String day,
                                    @RequestParam("lessonHour") String lessonHour,
                                    @RequestParam(value = "lessonId", required = false) Integer lessonId,
                                    @RequestParam(value = "examId", required = false) Integer examId,
                                    Model model) {
        // Kontrol: Hem lessonId hem examId birlikte gelmemeli
        if (lessonId != null && examId != null) {
            model.addAttribute("error", "Aynı anda hem ders hem sınav seçemezsiniz!");
            List<CourseSchedule> schedules = courseScheduleService.findByClassroomCode(classroomCode);
            List<Lesson> lessons = lessonService.findAll().stream()
                .filter(l -> l.getClassroom() != null && l.getClassroom().getSchool() != null 
                    && l.getClassroom().getSchool().getSchoolId() == currentSchoolId())
                .collect(java.util.stream.Collectors.toList());
            List<Exam> exams = examService.findAll().stream()
                .filter(e -> e.getLesson() != null && e.getLesson().getClassroom() != null 
                    && e.getLesson().getClassroom().getSchool() != null
                    && e.getLesson().getClassroom().getSchool().getSchoolId() == currentSchoolId())
                .collect(java.util.stream.Collectors.toList());
            model.addAttribute("classroomCode", classroomCode);
            model.addAttribute("schedules", schedules);
            model.addAttribute("lessons", lessons);
            model.addAttribute("exams", exams);
            return "admin/courseSchedule-form";
        }

        // Kontrol: En az birisi seçilmeli
        if (lessonId == null && examId == null) {
            model.addAttribute("error", "Lütfen bir ders veya sınav seçiniz!");
            List<CourseSchedule> schedules = courseScheduleService.findByClassroomCode(classroomCode);
            List<Lesson> lessons = lessonService.findAll().stream()
                .filter(l -> l.getClassroom() != null && l.getClassroom().getSchool() != null 
                    && l.getClassroom().getSchool().getSchoolId() == currentSchoolId())
                .collect(java.util.stream.Collectors.toList());
            List<Exam> exams = examService.findAll().stream()
                .filter(e -> e.getLesson() != null && e.getLesson().getClassroom() != null 
                    && e.getLesson().getClassroom().getSchool() != null
                    && e.getLesson().getClassroom().getSchool().getSchoolId() == currentSchoolId())
                .collect(java.util.stream.Collectors.toList());
            model.addAttribute("classroomCode", classroomCode);
            model.addAttribute("schedules", schedules);
            model.addAttribute("lessons", lessons);
            model.addAttribute("exams", exams);
            return "admin/courseSchedule-form";
        }

        // Çakışma kontrolü: Bu gün ve saatte zaten bir şey var mı?
        List<CourseSchedule> existingSchedules = courseScheduleService.findByClassroomCode(classroomCode);
        boolean conflict = existingSchedules.stream().anyMatch(s -> s.getDay().equals(day) && s.getLessonHour().equals(lessonHour));
        if (conflict) {
            model.addAttribute("error", "Bu gün ve saatte zaten bir ders veya sınav var!");
            List<CourseSchedule> schedules = courseScheduleService.findByClassroomCode(classroomCode);
            List<Lesson> lessons = lessonService.findAll().stream()
                .filter(l -> l.getClassroom() != null && l.getClassroom().getSchool() != null 
                    && l.getClassroom().getSchool().getSchoolId() == currentSchoolId())
                .collect(java.util.stream.Collectors.toList());
            List<Exam> exams = examService.findAll().stream()
                .filter(e -> e.getLesson() != null && e.getLesson().getClassroom() != null 
                    && e.getLesson().getClassroom().getSchool() != null
                    && e.getLesson().getClassroom().getSchool().getSchoolId() == currentSchoolId())
                .collect(java.util.stream.Collectors.toList());
            model.addAttribute("classroomCode", classroomCode);
            model.addAttribute("schedules", schedules);
            model.addAttribute("lessons", lessons);
            model.addAttribute("exams", exams);
            return "admin/courseSchedule-form";
        }

        CourseSchedule schedule = new CourseSchedule();
        schedule.setDay(day);
        schedule.setLessonHour(lessonHour);

        if (lessonId != null) {
            Lesson lesson = lessonService.findById(lessonId).orElse(null);
            schedule.setLesson(lesson);
        }

        if (examId != null) {
            Exam exam = examService.findById(examId).orElse(null);
            schedule.setExam(exam);
            // If exam has a lesson, automatically set it on the schedule
            if (exam != null && exam.getLesson() != null) {
                schedule.setLesson(exam.getLesson());
            }
        }

        // Set the classroom on the schedule
        Classroom classroom = classroomService.findById(classroomCode).orElse(null);
        if (classroom != null) {
            schedule.setClassroom(classroom);
        }

        courseScheduleService.save(schedule);
        return "redirect:/admin/classroom/" + classroomCode + "/courseschedule";
    }

    @GetMapping("/showCatalog")
    public String showCatalog(Model theModel) {
        if (currentSchoolId() == null) {
            return "redirect:/admin/school-login";
        }
        
        // Add school info to model
        School school = schoolService.findById(currentSchoolId()).orElse(null);
        theModel.addAttribute("school", school);
        
        return "admin/showCatalog";  // templates/persons/HomePage.html
    }


    // Örnek endpoint
    @RequestMapping("/dashboard")
    public String dashboard(Model model) {
        // Check if authenticated
        if (currentSchoolId() == null) {
            return "redirect:/admin/school-login";
        }

        // Get counts filtered by school
        int studentCount = studentService.findBySchoolId(currentSchoolId()).size();
        int teacherCount = (int) teacherService.findAll().stream()
            .filter(t -> t.getPerson() != null && t.getPerson().getSchool() != null 
                && t.getPerson().getSchool().getSchoolId() == currentSchoolId())
            .count();
        int personCount = (int) personService.findAll().stream()
            .filter(p -> p.getSchool() != null && p.getSchool().getSchoolId() == currentSchoolId())
            .count();
        int lessonCount = (int) lessonService.findAll().stream()
            .filter(l -> l.getClassroom() != null && l.getClassroom().getSchool() != null 
                && l.getClassroom().getSchool().getSchoolId() == currentSchoolId())
            .count();

        model.addAttribute("studentCount", studentCount);
        model.addAttribute("teacherCount", teacherCount);
        model.addAttribute("personCount", personCount);
        model.addAttribute("lessonCount", lessonCount);

        // Add school info
        School school = schoolService.findById(currentSchoolId()).orElse(null);
        model.addAttribute("school", school);

        System.out.println("Toplam öğrenci: " + studentCount);
        System.out.println("Toplam öğretmen: " + teacherCount);

        return "admin/dashboard"; // Thymeleaf view
    }


    // ==================== LESSON MANAGEMENT ====================
    @GetMapping("/lessonList")
    public String listLessons(Model theModel) {
        if (!isAuthenticated()) return "redirect:/admin/school-login";
        List<Lesson> lessons = lessonService.findAll().stream()
            .filter(l -> l.getClassroom() != null && l.getClassroom().getSchool() != null 
                && l.getClassroom().getSchool().getSchoolId() == currentSchoolId())
            .collect(java.util.stream.Collectors.toList());
        theModel.addAttribute("lessons", lessons);
        return "admin/lesson-List";
    }

    @GetMapping("/showFormForAddLesson")
    public String showFormForAddLesson(Model theModel) {
        Lesson lesson = new Lesson();
        List<Teacher> teachers = teacherService.findAll().stream()
            .filter(t -> t.getPerson() != null && t.getPerson().getSchool() != null 
                && t.getPerson().getSchool().getSchoolId() == currentSchoolId())
            .collect(java.util.stream.Collectors.toList());
        List<Classroom> classrooms = classroomService.findAll().stream()
            .filter(c -> c.getSchool() != null && c.getSchool().getSchoolId() == currentSchoolId())
            .collect(java.util.stream.Collectors.toList());
        theModel.addAttribute("lesson", lesson);
        theModel.addAttribute("teachers", teachers);
        theModel.addAttribute("classrooms", classrooms);
        return "admin/lesson-form";
    }

    @GetMapping("/showFormForUpdateLesson")
    public String showFormForUpdateLesson(@RequestParam("lessonId") int theId, Model theModel) {
        Lesson lesson = lessonService.findById(theId).orElse(new Lesson());
        List<Teacher> teachers = teacherService.findAll().stream()
            .filter(t -> t.getPerson() != null && t.getPerson().getSchool() != null 
                && t.getPerson().getSchool().getSchoolId() == currentSchoolId())
            .collect(java.util.stream.Collectors.toList());
        List<Classroom> classrooms = classroomService.findAll().stream()
            .filter(c -> c.getSchool() != null && c.getSchool().getSchoolId() == currentSchoolId())
            .collect(java.util.stream.Collectors.toList());
        theModel.addAttribute("lesson", lesson);
        theModel.addAttribute("teachers", teachers);
        theModel.addAttribute("classrooms", classrooms);
        return "admin/lesson-form";
    }

    @PostMapping("/saveLesson")
    public String saveLesson(@ModelAttribute("lesson") Lesson lesson,
                             @RequestParam(value = "classroomCode", required = false) String classroomCode,
                             @RequestParam(value = "teacherIds", required = false) List<Long> teacherIds,
                             Model model) {
        // Validate classroom is selected
        if (classroomCode == null || classroomCode.isEmpty()) {
            model.addAttribute("error", "Please select a classroom for this lesson!");
            List<Teacher> teachers = teacherService.findAll().stream()
                .filter(t -> t.getPerson() != null && t.getPerson().getSchool() != null 
                    && t.getPerson().getSchool().getSchoolId() == currentSchoolId())
                .collect(java.util.stream.Collectors.toList());
            List<Classroom> classrooms = classroomService.findAll().stream()
                .filter(c -> c.getSchool() != null && c.getSchool().getSchoolId() == currentSchoolId())
                .collect(java.util.stream.Collectors.toList());
            model.addAttribute("lesson", lesson);
            model.addAttribute("teachers", teachers);
            model.addAttribute("classrooms", classrooms);
            return "admin/lesson-form";
        }
        
        // Check for duplicate lesson code
        Optional<Lesson> existingLesson = lessonService.findByLessonCode(lesson.getLessonCode());
        if (existingLesson.isPresent()) {
            // If it's an update and the existing lesson is the same one, allow it
            if (lesson.getLessonId() == null || lesson.getLessonId() == 0 
                || !existingLesson.get().getLessonId().equals(lesson.getLessonId())) {
                model.addAttribute("error", "A lesson with code '" + lesson.getLessonCode() + "' already exists!");
                List<Teacher> teachers = teacherService.findAll().stream()
                    .filter(t -> t.getPerson() != null && t.getPerson().getSchool() != null 
                        && t.getPerson().getSchool().getSchoolId() == currentSchoolId())
                    .collect(java.util.stream.Collectors.toList());
                List<Classroom> classrooms = classroomService.findAll().stream()
                    .filter(c -> c.getSchool() != null && c.getSchool().getSchoolId() == currentSchoolId())
                    .collect(java.util.stream.Collectors.toList());
                model.addAttribute("lesson", lesson);
                model.addAttribute("teachers", teachers);
                model.addAttribute("classrooms", classrooms);
                return "admin/lesson-form";
            }
        }
        
        // Set classroom if provided
        if (classroomCode != null && !classroomCode.isEmpty()) {
            Classroom classroom = classroomService.findById(classroomCode).orElse(null);
            lesson.setClassroom(classroom);
        }
        
        // Set teachers if provided
        if (teacherIds != null && !teacherIds.isEmpty()) {
            Set<Teacher> teachers = new HashSet<>();
            for (Long teacherId : teacherIds) {
                Teacher teacher = teacherService.findById(teacherId).orElse(null);
                if (teacher != null) {
                    teachers.add(teacher);
                }
            }
            lesson.setTeachers(teachers);
        } else {
            // Clear teachers if none selected
            lesson.setTeachers(new HashSet<>());
        }
        
        try {
            lessonService.save(lesson);
            return "redirect:/admin/lessonList";
        } catch (Exception e) {
            // Handle trigger exception: teacher-lesson school mismatch
            String errorMessage = e.getMessage();
            if (errorMessage != null && errorMessage.contains("School mismatch")) {
                model.addAttribute("error", "Cannot assign teacher! Teachers can only be assigned to lessons in their own school.");
            } else {
                model.addAttribute("error", "Error saving lesson: " + (errorMessage != null ? errorMessage : "Unknown error"));
            }
            
            // Return to form with error
            List<Teacher> allTeachers = teacherService.findAll().stream()
                .filter(t -> t.getPerson() != null && t.getPerson().getSchool() != null 
                    && t.getPerson().getSchool().getSchoolId() == currentSchoolId())
                .collect(java.util.stream.Collectors.toList());
            List<Classroom> classrooms = classroomService.findAll().stream()
                .filter(c -> c.getSchool() != null && c.getSchool().getSchoolId() == currentSchoolId())
                .collect(java.util.stream.Collectors.toList());
            model.addAttribute("lesson", lesson);
            model.addAttribute("teachers", allTeachers);
            model.addAttribute("classrooms", classrooms);
            return "admin/lesson-form";
        }
    }

    @GetMapping("/deleteLesson")
    public String deleteLesson(@RequestParam("lessonId") int theId) {
        lessonService.delete(theId);
        return "redirect:/admin/lessonList";
    }

    // ==================== EXAM MANAGEMENT ====================
    @GetMapping("/examList")
    public String listExams(Model theModel) {
        if (!isAuthenticated()) return "redirect:/admin/school-login";
        List<Exam> theExams = examService.findAll().stream()
            .filter(e -> e.getLesson() != null && e.getLesson().getClassroom() != null 
                && e.getLesson().getClassroom().getSchool() != null
                && e.getLesson().getClassroom().getSchool().getSchoolId() == currentSchoolId())
            .collect(java.util.stream.Collectors.toList());
        theModel.addAttribute("exams", theExams);
        return "admin/exam-List";
    }

    @GetMapping("/showFormForAddExam")
    public String showFormForAddExam(Model theModel) {
        Exam exam = new Exam();
        List<Lesson> lessons = lessonService.findAll().stream()
            .filter(l -> l.getClassroom() != null && l.getClassroom().getSchool() != null 
                && l.getClassroom().getSchool().getSchoolId() == currentSchoolId())
            .collect(java.util.stream.Collectors.toList());
        theModel.addAttribute("exam", exam);
        theModel.addAttribute("lessons", lessons);
        return "admin/exam-form";
    }

    @GetMapping("/showFormForUpdateExam")
    public String showFormForUpdateExam(@RequestParam("examId") int theId, Model theModel) {
        Exam exam = examService.findById(theId).orElse(new Exam());
        List<Lesson> lessons = lessonService.findAll().stream()
            .filter(l -> l.getClassroom() != null && l.getClassroom().getSchool() != null 
                && l.getClassroom().getSchool().getSchoolId() == currentSchoolId())
            .collect(java.util.stream.Collectors.toList());
        theModel.addAttribute("exam", exam);
        theModel.addAttribute("lessons", lessons);
        return "admin/exam-form";
    }

    @PostMapping("/saveExam")
    public String saveExam(@ModelAttribute("exam") Exam exam, @RequestParam(value = "lessonId", required = false) Integer lessonId) {
        if (lessonId != null && lessonId > 0) {
            Lesson lesson = lessonService.findById(lessonId).orElse(null);
            exam.setLesson(lesson);
        } else {
            exam.setLesson(null);
        }
        examService.save(exam);
        return "redirect:/admin/examList";
    }

    @GetMapping("/deleteExam")
    public String deleteExam(@RequestParam("examId") int theId) {
        // Önce bu exam'a ait tüm exam result'ları sil
        examResultService.deleteByExamId(theId);
        // CourseSchedule'daki exam referanslarını temizle
        courseScheduleService.clearExamFromCourseSchedules(theId);
        // Sonra exam'ı sil
        examService.delete(theId);
        return "redirect:/admin/examList";
    }

    // ==================== TEACHER MANAGEMENT ====================
    @GetMapping("/teacherList")
    public String listTeachers(Model theModel) {
        if (!isAuthenticated()) return "redirect:/admin/school-login";
        List<Teacher> teachers = teacherService.findAll().stream()
            .filter(t -> t.getPerson() != null && t.getPerson().getSchool() != null 
                && t.getPerson().getSchool().getSchoolId() == currentSchoolId())
            .collect(java.util.stream.Collectors.toList());
        theModel.addAttribute("teachers", teachers);
        return "admin/teacher-List";
    }

    @GetMapping("/showFormForAddTeacher")
    public String showFormForAddTeacher(Model theModel) {
        Teacher teacher = new Teacher();
        teacher.setPerson(new Person());
        theModel.addAttribute("teacher", teacher);
        return "admin/teacher-form";
    }

    @GetMapping("/showFormForUpdateTeacher")
    public String showFormForUpdateTeacher(@RequestParam("teacherId") Long theId, Model theModel) {
        Teacher teacher = teacherService.findById(theId).orElse(new Teacher());
        if (teacher.getPerson() == null) {
            teacher.setPerson(new Person());
        }
        theModel.addAttribute("teacher", teacher);
        return "admin/teacher-form";
    }

    @PostMapping("/saveTeacher")
    public String saveTeacher(@ModelAttribute("teacher") Teacher teacher, 
                            org.springframework.web.servlet.mvc.support.RedirectAttributes redirectAttributes) {
        try {
            // Set school for new persons
            if (teacher.getPerson() != null && teacher.getPerson().getSchool() == null && currentSchoolId() != null) {
                School school = schoolService.findById(currentSchoolId()).orElse(null);
                teacher.getPerson().setSchool(school);
            }
            // Save the person first
            Person savedPerson = personService.save(teacher.getPerson());
            teacher.setPerson(savedPerson);
            teacherService.save(teacher);
            return "redirect:/admin/teacherList";
        } catch (org.springframework.dao.DataIntegrityViolationException e) {
            String errorMessage = "Error: ";
            if (e.getMessage().contains("sicil_no")) {
                errorMessage += "This employee number (Sicil No) is already in use. Please use a different number.";
            } else if (e.getMessage().contains("unique") || e.getMessage().contains("duplicate")) {
                errorMessage += "This value already exists in the system. Please use a different value.";
            } else {
                errorMessage += "Unable to save teacher. Please check your input.";
            }
            redirectAttributes.addFlashAttribute("errorMessage", errorMessage);
            redirectAttributes.addFlashAttribute("teacher", teacher);
            return "redirect:/admin/showFormForAddTeacher";
        }
    }

    @GetMapping("/deleteTeacher")
    public String deleteTeacher(@RequestParam("teacherId") Long theId, Model model) {
        try {
            // With cascade = CascadeType.ALL, deleting teacher cascades to person
            Teacher teacher = teacherService.findById(theId).orElse(null);
            if (teacher != null) {
                teacherService.delete(theId);
            }
            return "redirect:/admin/teacherList";
        } catch (Exception e) {
            // Handle trigger exception: teacher with assigned lessons
            String errorMessage = e.getMessage();
            if (errorMessage != null && (errorMessage.contains("Cannot delete teacher") || errorMessage.contains("lesson"))) {
                model.addAttribute("error", "Cannot delete teacher! This teacher has assigned lessons. Please reassign the lessons first.");
            } else {
                model.addAttribute("error", "Error deleting teacher: " + (errorMessage != null ? errorMessage : "Unknown error"));
            }
            
            // Reload teacher list with error
            List<Teacher> teachers = teacherService.findAll().stream()
                .filter(t -> t.getPerson() != null && t.getPerson().getSchool() != null 
                    && t.getPerson().getSchool().getSchoolId() == currentSchoolId())
                .collect(java.util.stream.Collectors.toList());
            model.addAttribute("teachers", teachers);
            return "admin/teacher-List";
        }
    }

    // ==================== PARENT MANAGEMENT ====================
    @GetMapping("/parentList")
    public String listParents(Model theModel) {
        if (!isAuthenticated()) return "redirect:/admin/school-login";
        List<Parent> parents = parentService.findAll().stream()
            .filter(p -> p.getPerson() != null && p.getPerson().getSchool() != null 
                && p.getPerson().getSchool().getSchoolId() == currentSchoolId())
            .collect(java.util.stream.Collectors.toList());
        theModel.addAttribute("parents", parents);
        return "admin/parent-List";
    }

    @GetMapping("/showFormForAddParent")
    public String showFormForAddParent(Model theModel) {
        Parent parent = new Parent();
        parent.setPerson(new Person());
        theModel.addAttribute("parent", parent);
        return "admin/parent-form";
    }

    @GetMapping("/showFormForUpdateParent")
    public String showFormForUpdateParent(@RequestParam("parentId") Long theId, Model theModel) {
        Parent parent = parentService.findById(theId).orElse(new Parent());
        if (parent.getPerson() == null) {
            parent.setPerson(new Person());
        }
        theModel.addAttribute("parent", parent);
        return "admin/parent-form";
    }

    @PostMapping("/saveParent")
    public String saveParent(@ModelAttribute("parent") Parent parent) {
        // Set school for new persons
        if (parent.getPerson() != null && parent.getPerson().getSchool() == null && currentSchoolId() != null) {
            School school = schoolService.findById(currentSchoolId()).orElse(null);
            parent.getPerson().setSchool(school);
        }
        // Save the person first
        Person savedPerson = personService.save(parent.getPerson());
        parent.setPerson(savedPerson);
        parentService.save(parent);
        return "redirect:/admin/parentList";
    }

    @GetMapping("/deleteParent")
    public String deleteParent(@RequestParam("parentId") Long theId) {
        // With cascade = CascadeType.ALL, deleting parent cascades to person
        Parent parent = parentService.findById(theId).orElse(null);
        if (parent != null) {
            parentService.delete(theId);
        }
        return "redirect:/admin/parentList";
    }

    // Add student to parent with relationship
    @GetMapping("/showAddStudentToParent")
    public String showAddStudentToParent(@RequestParam("parentId") Long parentId, Model model) {
        if (!isAuthenticated()) return "redirect:/admin/school-login";
        
        Parent parent = parentService.findById(parentId).orElse(null);
        if (parent == null) {
            return "redirect:/admin/parentList";
        }
        
        // Get all students from this school
        List<Student> students = studentService.findBySchoolId(currentSchoolId());
        
        // Get existing relationships
        List<ParentStudent> existingRelations = parentStudentService.findByParentId(parentId);
        
        model.addAttribute("parent", parent);
        model.addAttribute("students", students);
        model.addAttribute("existingRelations", existingRelations);
        return "admin/parent-add-student";
    }
    
    @PostMapping("/addStudentToParent")
    public String addStudentToParent(@RequestParam("parentId") Long parentId,
                                     @RequestParam("studentId") Long studentId,
                                     @RequestParam("relationshipDegree") String relationshipDegree) {
        Parent parent = parentService.findById(parentId).orElse(null);
        Student student = studentService.findById(studentId).orElse(null);
        
        if (parent != null && student != null) {
            ParentStudent ps = new ParentStudent(parent, student, relationshipDegree);
            parentStudentService.save(ps);
        }
        
        return "redirect:/admin/showAddStudentToParent?parentId=" + parentId;
    }
    
    @GetMapping("/removeStudentFromParent")
    public String removeStudentFromParent(@RequestParam("parentId") Long parentId,
                                          @RequestParam("studentId") Long studentId) {
        parentStudentService.deleteByParentIdAndStudentId(parentId, studentId);
        return "redirect:/admin/showAddStudentToParent?parentId=" + parentId;
    }

    // ==================== MANAGER MANAGEMENT ====================
    @GetMapping("/managerList")
    public String listManagers(Model theModel) {
        if (!isAuthenticated()) return "redirect:/admin/school-login";
        List<Manager> managers = managerService.findAll().stream()
            .filter(m -> m.getPerson() != null && m.getPerson().getSchool() != null 
                && m.getPerson().getSchool().getSchoolId() == currentSchoolId())
            .collect(java.util.stream.Collectors.toList());
        theModel.addAttribute("managers", managers);
        return "admin/manager-List";
    }

    @GetMapping("/showFormForAddManager")
    public String showFormForAddManager(Model theModel) {
        Manager manager = new Manager();
        manager.setPerson(new Person());
        theModel.addAttribute("manager", manager);
        return "admin/manager-form";
    }

    @GetMapping("/showFormForUpdateManager")
    public String showFormForUpdateManager(@RequestParam("managerId") Long theId, Model theModel) {
        Manager manager = managerService.findById(theId).orElse(new Manager());
        if (manager.getPerson() == null) {
            manager.setPerson(new Person());
        }
        theModel.addAttribute("manager", manager);
        return "admin/manager-form";
    }

    @PostMapping("/saveManager")
    public String saveManager(@ModelAttribute("manager") Manager manager,
                            org.springframework.web.servlet.mvc.support.RedirectAttributes redirectAttributes) {
        try {
            // Set school for new persons
            if (manager.getPerson() != null && manager.getPerson().getSchool() == null && currentSchoolId() != null) {
                School school = schoolService.findById(currentSchoolId()).orElse(null);
                manager.getPerson().setSchool(school);
            }
            // Save the person first
            Person savedPerson = personService.save(manager.getPerson());
            manager.setPerson(savedPerson);
            managerService.save(manager);
            return "redirect:/admin/managerList";
        } catch (org.springframework.dao.DataIntegrityViolationException e) {
            String errorMessage = "Error: ";
            if (e.getMessage().contains("sicil_no")) {
                errorMessage += "This employee number (Sicil No) is already in use. Please use a different number.";
            } else if (e.getMessage().contains("unique") || e.getMessage().contains("duplicate")) {
                errorMessage += "This value already exists in the system. Please use a different value.";
            } else {
                errorMessage += "Unable to save manager. Please check your input.";
            }
            redirectAttributes.addFlashAttribute("errorMessage", errorMessage);
            redirectAttributes.addFlashAttribute("manager", manager);
            return "redirect:/admin/showFormForAddManager";
        }
    }

    @GetMapping("/deleteManager")
    public String deleteManager(@RequestParam("managerId") Long theId) {
        // With cascade = CascadeType.ALL, deleting manager cascades to person
        Manager manager = managerService.findById(theId).orElse(null);
        if (manager != null) {
            managerService.delete(theId);
        }
        return "redirect:/admin/managerList";
    }

    // =================== STUDENT MANAGEMENT ====================

    @GetMapping("/studentList")
    public String listStudents(Model theModel){
        if (!isAuthenticated()) return "redirect:/admin/school-login";
        List<Student> theStudents = studentService.findBySchoolId(currentSchoolId());
        
        // Calculate average scores for each student
        java.util.Map<Long, Double> studentAverages = new java.util.HashMap<>();
        for (Student student : theStudents) {
            List<ExamResult> examResults = examResultService.findByStudentId(student.getId().intValue());
            if (examResults != null && !examResults.isEmpty()) {
                double sum = examResults.stream()
                    .filter(er -> er.getScore() != null)
                    .mapToDouble(ExamResult::getScore)
                    .sum();
                long count = examResults.stream()
                    .filter(er -> er.getScore() != null)
                    .count();
                double average = count > 0 ? sum / count : 0.0;
                studentAverages.put(student.getId(), average);
            } else {
                studentAverages.put(student.getId(), 0.0);
            }
        }

        theModel.addAttribute("students" , theStudents);
        theModel.addAttribute("studentAverages", studentAverages);
        return "admin/student-List";
    }

    @GetMapping("/showFormForAddStudent")
    public String showFormForAddStudent(Model model) {
        Student student = new Student();
        student.setPerson(new Person()); // Person objesini oluştur, formda doldurulabilir
        
        List<Classroom> classrooms = classroomService.findAll().stream()
            .filter(c -> c.getSchool() != null && c.getSchool().getSchoolId() == currentSchoolId())
            .collect(java.util.stream.Collectors.toList());
        model.addAttribute("student", student);
        model.addAttribute("classrooms", classrooms);
        return "admin/student-form";
    }


    @PostMapping("/saveStudent")
    public String saveStudent(@ModelAttribute("student") Student student, 
                             @RequestParam("classroomCode") String classroomCode,
                             Model model) {
        try {
            // Set school for new persons
            if (student.getPerson() != null && student.getPerson().getSchool() == null && currentSchoolId() != null) {
                School school = schoolService.findById(currentSchoolId()).orElse(null);
                student.getPerson().setSchool(school);
            }
            // Save the person first
            Person savedPerson = personService.save(student.getPerson());
            student.setPerson(savedPerson);
            
            // Set the classroom
            Classroom classroom = classroomService.findById(classroomCode).orElse(null);
            student.setClassroom(classroom);
            
            studentService.save(student);
            return "redirect:/admin/studentList";
        } catch (Exception e) {
            // Check if it's a database constraint error (trigger exception)
            String errorMessage = e.getMessage();
            if (errorMessage != null && errorMessage.contains("Classroom capacity exceeded")) {
                // Extract the meaningful part of the error message
                model.addAttribute("error", "Cannot add student: The selected classroom has reached its maximum capacity!");
            } else if (errorMessage != null && errorMessage.contains("capacity")) {
                model.addAttribute("error", "Cannot add student: Classroom capacity limit reached!");
            } else {
                model.addAttribute("error", "Error saving student: " + (errorMessage != null ? errorMessage : "Unknown error"));
            }
            
            // Return to form with error
            List<Classroom> classrooms = classroomService.findAll().stream()
                .filter(c -> c.getSchool() != null && c.getSchool().getSchoolId() == currentSchoolId())
                .collect(java.util.stream.Collectors.toList());
            model.addAttribute("student", student);
            model.addAttribute("classrooms", classrooms);
            return "admin/student-form";
        }
    }
    @GetMapping("/showFormForUpdateStudent")
    public String showFormForUpdateStudent(@RequestParam("studentId") int theId, Model theModel) {
        // get the student from the service
        Student theStudent = studentService.findById((long) theId).orElse(new Student());
        if (theStudent.getPerson() == null) {
            theStudent.setPerson(new Person());
        }
        List<Classroom> classrooms = classroomService.findAll().stream()
            .filter(c -> c.getSchool() != null && c.getSchool().getSchoolId() == currentSchoolId())
            .collect(java.util.stream.Collectors.toList());
        theModel.addAttribute("student", theStudent);
        theModel.addAttribute("classrooms", classrooms);
        return "admin/student-form";
    }

    @GetMapping("/deleteStudent")
    public String deleteStudent(@RequestParam("studentId") Long theId) {
        // Önce bu student'a ait tüm exam result'larını sil
        examResultService.deleteByStudentId(theId.intValue());
        // With cascade = CascadeType.ALL, deleting student cascades to person
        Student student = studentService.findById(theId).orElse(null);
        if (student != null) {
            studentService.delete(theId);
        }
        return "redirect:/admin/studentList";
    }

    // ==================== STAFF MANAGEMENT ====================
    @GetMapping("/staffList")
    public String listStaff(Model theModel) {
        if (!isAuthenticated()) return "redirect:/admin/school-login";
        List<Staff> staff = staffService.findAll().stream()
            .filter(s -> s.getPerson() != null && s.getPerson().getSchool() != null 
                && s.getPerson().getSchool().getSchoolId() == currentSchoolId())
            .collect(java.util.stream.Collectors.toList());
        theModel.addAttribute("staff", staff);
        return "admin/staff-List";
    }

    @GetMapping("/showFormForAddStaff")
    public String showFormForAddStaff(Model theModel) {
        Staff staff = new Staff();
        staff.setPerson(new Person());
        theModel.addAttribute("staff", staff);
        return "admin/staff-form";
    }

    @GetMapping("/showFormForUpdateStaff")
    public String showFormForUpdateStaff(@RequestParam("staffId") Long theId, Model theModel) {
        Staff staff = staffService.findById(theId).orElse(new Staff());
        if (staff.getPerson() == null) {
            staff.setPerson(new Person());
        }
        theModel.addAttribute("staff", staff);
        return "admin/staff-form";
    }

    @PostMapping("/saveStaff")
    public String saveStaff(@ModelAttribute("staff") Staff staff,
                          org.springframework.web.servlet.mvc.support.RedirectAttributes redirectAttributes) {
        try {
            // Set school for new persons
            if (staff.getPerson() != null && staff.getPerson().getSchool() == null && currentSchoolId() != null) {
                School school = schoolService.findById(currentSchoolId()).orElse(null);
                staff.getPerson().setSchool(school);
            }
            // Save the person first
            Person savedPerson = personService.save(staff.getPerson());
            staff.setPerson(savedPerson);
            staffService.save(staff);
            return "redirect:/admin/staffList";
        } catch (org.springframework.dao.DataIntegrityViolationException e) {
            String errorMessage = "Error: ";
            if (e.getMessage().contains("sicil_no")) {
                errorMessage += "This employee number (Sicil No) is already in use. Please use a different number.";
            } else if (e.getMessage().contains("unique") || e.getMessage().contains("duplicate")) {
                errorMessage += "This value already exists in the system. Please use a different value.";
            } else {
                errorMessage += "Unable to save staff. Please check your input.";
            }
            redirectAttributes.addFlashAttribute("errorMessage", errorMessage);
            redirectAttributes.addFlashAttribute("staff", staff);
            return "redirect:/admin/showFormForAddStaff";
        }
    }

    @GetMapping("/deleteStaff")
    public String deleteStaff(@RequestParam("staffId") Long theId) {
        // With cascade = CascadeType.ALL, deleting staff cascades to person
        Staff staff = staffService.findById(theId).orElse(null);
        if (staff != null) {
            staffService.delete(theId);
        }
        return "redirect:/admin/staffList";
    }

    // ==================== SCHOOL MANAGEMENT ====================
    @GetMapping("/schoolList")
    public String listSchools(Model theModel) {
        if (!isAuthenticated()) return "redirect:/admin/school-login";
        List<School> schools = schoolService.findAll();
        theModel.addAttribute("schools", schools);
        return "admin/school-List";
    }

    @GetMapping("/showFormForAddSchool")
    public String showFormForAddSchool(Model theModel) {
        School school = new School();
        theModel.addAttribute("school", school);
        return "admin/school-form";
    }

    @GetMapping("/showFormForUpdateSchool")
    public String showFormForUpdateSchool(@RequestParam("schoolId") int theId, Model theModel) {
        School school = schoolService.findById(theId).orElse(new School());
        theModel.addAttribute("school", school);
        return "admin/school-form";
    }

    @PostMapping("/saveSchool")
    public String saveSchool(@ModelAttribute("school") School school) {
        schoolService.save(school);
        return "redirect:/admin/school-login";
    }

    @GetMapping("/deleteSchool")
    public String deleteSchool(@RequestParam("schoolId") int theId) {
        schoolService.delete(theId);
        return "redirect:/admin/schoolList";
    }

    // ==================== CLASSROOM MANAGEMENT ====================
    @GetMapping("/classroomList")
    public String listClassrooms(Model theModel) {
        if (!isAuthenticated()) return "redirect:/admin/school-login";
        List<Classroom> classrooms = classroomService.findAll().stream()
            .filter(c -> c.getSchool() != null && c.getSchool().getSchoolId() == currentSchoolId())
            .collect(java.util.stream.Collectors.toList());
        
        // Get classroom averages from stored procedure
        java.util.Map<String, Double> classroomAverages = classroomService.getClassroomAverages();
        
        theModel.addAttribute("classrooms", classrooms);
        theModel.addAttribute("classroomAverages", classroomAverages);
        return "admin/classroom-List";
    }

    @GetMapping("/showFormForAddClassroom")
    public String showFormForAddClassroom(Model theModel) {
        Classroom classroom = new Classroom();
        theModel.addAttribute("classroom", classroom);
        return "admin/classroom-form";
    }

    @GetMapping("/showFormForUpdateClassroom")
    public String showFormForUpdateClassroom(@RequestParam("classroomCode") String theCode, Model theModel) {
        Classroom classroom = classroomService.findById(theCode).orElse(new Classroom());
        theModel.addAttribute("classroom", classroom);
        return "admin/classroom-form";
    }

    @PostMapping("/saveClassroom")
    public String saveClassroom(@ModelAttribute("classroom") Classroom classroom,
                                @RequestParam(value = "originalClassroomCode", required = false) String originalClassroomCode) {
        // Set school for new classrooms
        if (classroom.getSchool() == null && currentSchoolId() != null) {
            School school = schoolService.findById(currentSchoolId()).orElse(null);
            classroom.setSchool(school);
        }
        
        // Handle classroom code change during update
        if (originalClassroomCode != null && !originalClassroomCode.equals(classroom.getClassroomCode())) {
            // Classroom code changed - need to delete old and create new
            Optional<Classroom> oldClassroom = classroomService.findById(originalClassroomCode);
            if (oldClassroom.isPresent()) {
                // Copy relationships from old classroom to new one
                Classroom old = oldClassroom.get();
                if (classroom.getSchool() == null) {
                    classroom.setSchool(old.getSchool());
                }
                if (classroom.getExam() == null) {
                    classroom.setExam(old.getExam());
                }
                // Delete the old classroom
                classroomService.delete(originalClassroomCode);
            }
        }
        
        classroomService.save(classroom);
        return "redirect:/admin/classroomList";
    }

    @GetMapping("/deleteClassroom")
    public String deleteClassroom(@RequestParam("classroomCode") String theCode) {
        classroomService.delete(theCode);
        return "redirect:/admin/classroomList";
    }

    // ==================== EXAM RESULT MANAGEMENT ====================
    @GetMapping("/examResults")
    public String listExamResults(@RequestParam("studentId") Long studentId, Model model) {
        Student student = studentService.findById(studentId).orElse(null);
        List<ExamResult> examResults = examResultService.findByStudentId(studentId.intValue());
        
        model.addAttribute("student", student);
        model.addAttribute("examResults", examResults);
        return "admin/examResults-List";
    }

    @GetMapping("/showFormForAddExamResult")
    public String showFormForAddExamResult(@RequestParam("studentId") Long studentId, Model model) {
        Student student = studentService.findById(studentId).orElse(null);
        ExamResult examResult = new ExamResult();
        examResult.setStudent(student);
        
        List<Exam> availableExams = new ArrayList<>();
        
        if (student != null && student.getClassroom() != null) {
            // Student'ın classroom'unun CourseSchedule'sinde olan sınavları bul
            List<CourseSchedule> schedules = courseScheduleService.findByClassroomCode(student.getClassroom().getClassroomCode());
            
            // CourseSchedule'de olan exam'ları topla
            Set<Integer> examIdsInSchedule = schedules.stream()
                .filter(s -> s.getExam() != null)
                .map(s -> s.getExam().getExamId())
                .collect(Collectors.toSet());
            
            // Bu öğrencinin zaten sonucu olan sınavları bul
            List<ExamResult> studentResults = examResultService.findByStudentId(studentId.intValue());
            Set<Integer> examIdsWithResults = studentResults.stream()
                .map(er -> er.getExam().getExamId())
                .collect(Collectors.toSet());
            
            // Schedule'de olan ancak sonucu olmayan sınavları göster
            List<Exam> allExams = examService.findAll();
            availableExams = allExams.stream()
                .filter(exam -> examIdsInSchedule.contains(exam.getExamId()) && !examIdsWithResults.contains(exam.getExamId()))
                .collect(Collectors.toList());
        }
        
        model.addAttribute("student", student);
        model.addAttribute("examResult", examResult);
        model.addAttribute("exams", availableExams);
        return "admin/examResult-form";
    }

    @GetMapping("/showFormForUpdateExamResult")
    public String showFormForUpdateExamResult(@RequestParam("resultId") int resultId, Model model) {
        ExamResult examResult = examResultService.findById(resultId).orElse(new ExamResult());
        List<Exam> exams = examService.findAll().stream()
            .filter(e -> e.getLesson() != null && e.getLesson().getClassroom() != null 
                && e.getLesson().getClassroom().getSchool() != null
                && e.getLesson().getClassroom().getSchool().getSchoolId() == currentSchoolId())
            .collect(java.util.stream.Collectors.toList());
        
        model.addAttribute("examResult", examResult);
        model.addAttribute("student", examResult.getStudent());
        model.addAttribute("exams", exams);
        return "admin/examResult-form";
    }

    @PostMapping("/saveExamResult")
    public String saveExamResult(@ModelAttribute("examResult") ExamResult examResult, 
                                  @RequestParam("examId") int examId,
                                  Model model) {
        try {
            Long studentId = examResult.getStudent().getId();
            
            // Kontrol: Bu öğrencinin bu sınava ait bir sonucu zaten var mı?
            List<ExamResult> studentResults = examResultService.findByStudentId(studentId.intValue());
            boolean examResultExists = studentResults.stream()
                .anyMatch(er -> er.getExam().getExamId() == examId);
            
            if (examResultExists) {
                // Tekrar yükleme sayfasına dön (error mesajı göstermek için model'e hata ekleyebilirsin)
                return "redirect:/admin/examResults?studentId=" + studentId + "&error=exists";
            }
            
            Exam exam = examService.findById(examId).orElse(null);
            examResult.setExam(exam);
            examResultService.save(examResult);
            return "redirect:/admin/examResults?studentId=" + studentId;
        } catch (Exception e) {
            // Handle trigger exception: invalid score range
            String errorMessage = e.getMessage();
            Long studentId = examResult.getStudent().getId();
            
            if (errorMessage != null && errorMessage.contains("Invalid exam score")) {
                model.addAttribute("error", "Invalid exam score! Score must be between 0 and 100.");
            } else if (errorMessage != null && errorMessage.contains("score")) {
                model.addAttribute("error", "Invalid score value. Please enter a score between 0 and 100.");
            } else {
                model.addAttribute("error", "Error saving exam result: " + (errorMessage != null ? errorMessage : "Unknown error"));
            }
            
            // Return to results page with error
            return "redirect:/admin/examResults?studentId=" + studentId + "&error=invalid";
        }
    }

    @PostMapping("/updateExamResult")
    public String updateExamResult(@ModelAttribute("examResult") ExamResult examResult,
                                    @RequestParam("examId") int examId) {
        Exam exam = examService.findById(examId).orElse(null);
        examResult.setExam(exam);
        Long studentId = examResult.getStudent().getId();
        examResultService.update(examResult);
        return "redirect:/admin/examResults?studentId=" + studentId;
    }

    @GetMapping("/deleteExamResult")
    public String deleteExamResult(@RequestParam("resultId") int resultId, @RequestParam("studentId") Long studentId) {
        examResultService.delete(resultId);
        return "redirect:/admin/examResults?studentId=" + studentId;
    }

    // ==================== ABSENCE MANAGEMENT ====================
    @GetMapping("/absenceList")
    public String listAbsences(@RequestParam("studentId") Long studentId, Model model) {
        if (!isAuthenticated()) return "redirect:/admin/school-login";
        Student student = studentService.findById(studentId).orElse(new Student());
        Set<Absence> absences = student.getAbsences();
        
        model.addAttribute("student", student);
        model.addAttribute("absences", absences);
        return "admin/absence-List";
    }

    @GetMapping("/showFormForAddAbsence")
    public String showFormForAddAbsence(@RequestParam("studentId") Long studentId, Model model) {
        Student student = studentService.findById(studentId).orElse(new Student());
        Absence absence = new Absence();
        absence.setStudent(student);
        
        model.addAttribute("student", student);
        model.addAttribute("absence", absence);
        return "admin/absence-form";
    }

    @GetMapping("/showFormForUpdateAbsence")
    public String showFormForUpdateAbsence(@RequestParam("absenceId") Long absenceId, @RequestParam("studentId") Long studentId, Model model) {
        Student student = studentService.findById(studentId).orElse(new Student());
        Absence absence = absenceService.findById(absenceId).orElse(new Absence());
        
        model.addAttribute("student", student);
        model.addAttribute("absence", absence);
        return "admin/absence-form";
    }

    @PostMapping("/saveAbsence")
    public String saveAbsence(@ModelAttribute("absence") Absence absence, 
                             @RequestParam("studentId") Long studentId,
                             Model model) {
        try {
            Student student = studentService.findById(studentId).orElse(null);
            absence.setStudent(student);
            absenceService.save(absence);
            return "redirect:/admin/absenceList?studentId=" + studentId;
        } catch (Exception e) {
            // Handle any trigger exceptions
            String errorMessage = e.getMessage();
            model.addAttribute("error", "Error saving absence: " + (errorMessage != null ? errorMessage : "Unknown error"));
            
            // Note: The excessive absence trigger creates discipline records automatically
            // It doesn't throw an error, it just logs a notice
            return "redirect:/admin/absenceList?studentId=" + studentId + "&error=save";
        }
    }

    @GetMapping("/deleteAbsence")
    public String deleteAbsence(@RequestParam("absenceId") Long absenceId, @RequestParam("studentId") Long studentId) {
        absenceService.delete(absenceId);
        return "redirect:/admin/absenceList?studentId=" + studentId;
    }

    // ==================== DISCIPLINE RECORD MANAGEMENT ====================
    @GetMapping("/disciplineRecordList")
    public String listDisciplineRecords(@RequestParam("studentId") Long studentId, Model model) {
        if (!isAuthenticated()) return "redirect:/admin/school-login";
        Student student = studentService.findById(studentId).orElse(new Student());
        Set<DisciplineRecord> disciplineRecords = student.getDisciplineRecords();
        
        model.addAttribute("student", student);
        model.addAttribute("disciplineRecords", disciplineRecords);
        return "admin/disciplineRecord-List";
    }

    @GetMapping("/showFormForAddDisciplineRecord")
    public String showFormForAddDisciplineRecord(@RequestParam("studentId") Long studentId, Model model) {
        Student student = studentService.findById(studentId).orElse(new Student());
        DisciplineRecord disciplineRecord = new DisciplineRecord();
        disciplineRecord.setStudent(student);
        
        model.addAttribute("student", student);
        model.addAttribute("disciplineRecord", disciplineRecord);
        return "admin/disciplineRecord-form";
    }

    @GetMapping("/showFormForUpdateDisciplineRecord")
    public String showFormForUpdateDisciplineRecord(@RequestParam("disciplineRecordId") Long disciplineRecordId, @RequestParam("studentId") Long studentId, Model model) {
        Student student = studentService.findById(studentId).orElse(new Student());
        DisciplineRecord disciplineRecord = disciplineRecordService.findById(disciplineRecordId).orElse(new DisciplineRecord());
        
        model.addAttribute("student", student);
        model.addAttribute("disciplineRecord", disciplineRecord);
        return "admin/disciplineRecord-form";
    }

    @PostMapping("/saveDisciplineRecord")
    public String saveDisciplineRecord(@ModelAttribute("disciplineRecord") DisciplineRecord disciplineRecord, @RequestParam("studentId") Long studentId) {
        Student student = studentService.findById(studentId).orElse(null);
        disciplineRecord.setStudent(student);
        disciplineRecordService.save(disciplineRecord);
        return "redirect:/admin/disciplineRecordList?studentId=" + studentId;
    }

    @GetMapping("/deleteDisciplineRecord")
    public String deleteDisciplineRecord(@RequestParam("disciplineRecordId") Long disciplineRecordId, @RequestParam("studentId") Long studentId) {
        disciplineRecordService.delete(disciplineRecordId);
        return "redirect:/admin/disciplineRecordList?studentId=" + studentId;
    }
}
