# School Management System

![Java 21](https://img.shields.io/badge/Java-21-ED8B00?logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5-6DB33F?logo=springboot&logoColor=white)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-16%2B-336791?logo=postgresql&logoColor=white)
![Thymeleaf](https://img.shields.io/badge/Thymeleaf-server--side-005F0F?logo=thymeleaf&logoColor=white)
[![CI](https://github.com/Mastena12/school-management-system/actions/workflows/ci.yml/badge.svg)](https://github.com/Mastena12/school-management-system/actions/workflows/ci.yml)
![License](https://img.shields.io/badge/license-MIT-blue)

A web-based **school management system** built with Spring Boot MVC, Thymeleaf and PostgreSQL. A school administrator manages classrooms, lessons, teachers, students, parents, exams, attendance and discipline records from one dashboard. Registered users can message each other in **real time**.

A large part of the business logic lives in the database itself: **triggers, stored procedures with cursors and integrity constraints** enforce the rules no matter which client writes the data.

## Features

### Admin panel (per school)
- **School login.** Each school has its own admin password, and all data is scoped to the signed-in school.
- **CRUD for 14+ entities:** schools, classrooms, classes, lessons, course schedules, teachers, students, parents, managers, staff, exams, exam results, absences and discipline records.
- **Parent ↔ student linking:** a many-to-many relation managed from the UI.
- **Dashboard** with per-school counts and a catalogue view of the school's data.
- **Weekly course schedule** per classroom.

### Users and messaging
- Registration and login for people (students, teachers, parents, ...).
- **One-to-one chat** with live updates pushed through **Server-Sent Events (SSE)**.

### Database logic (PostgreSQL)
| Object | What it does |
|---|---|
| `trg_check_classroom_capacity` | Rejects enrolling a student into a full classroom |
| `trg_track_excessive_absences` | Automatically opens a discipline record once a student reaches 5+ absences |
| `trg_validate_exam_score` | Keeps exam scores in the 0–100 range |
| `trg_audit_exam_result_changes` | Writes an audit trail whenever a grade is changed |
| `trg_prevent_teacher_deletion` | Blocks deleting a teacher who still has lessons |
| `trg_validate_teacher_lesson_school` | A teacher can only teach lessons at their own school |
| `trg_update_person_timestamp` | Maintains `last_updated` automatically |
| `generate_classroom_report()` | Stored **procedure using a cursor** to build a per-classroom report |
| `calculate_classroom_averages()` / `get_classroom_averages()` | Classroom grade averages |
| `add-database-constraints.sql` | CHECK, UNIQUE and FK constraints for data integrity |

### Security
- Passwords (users and schools) are stored as **BCrypt** hashes. Legacy plain-text rows are upgraded automatically on the next successful login.
- The admin login state lives in the **user's own HTTP session**, never in shared controller state.
- Database credentials come from **environment variables**, not from source control.

## Tech stack

- **Java 21**, **Spring Boot 3.5** (Web MVC, Data JPA / Hibernate)
- **Thymeleaf** server-side templates + Bootstrap
- **PostgreSQL** with PL/pgSQL triggers, functions and procedures
- **Server-Sent Events** for real-time chat
- **spring-security-crypto** (BCrypt)
- Maven (wrapper included)

## Architecture

```
src/main/java/com/School_Auto/demo
├── controller/   AdminController (admin panel), PersonController (users & chat), SSEController (live messages)
├── service/      Business logic, one service per aggregate (interface + implementation)
├── dao/          Data access with JPA EntityManager / JPQL
├── entity/       JPA entities and their relationships
└── security/     Password hashing (BCrypt)

src/main/resources
├── templates/admin/     Admin panel pages (lists + forms)
├── templates/persons/   Registration, login, people directory, chat
└── application.properties

database/
├── populate-database.sql             Sample data (3 schools, teachers, students, ...)
├── database-triggers.sql             Triggers listed above
├── classroom-average-procedure.sql   Cursor-based reporting procedure
├── add-database-constraints.sql      Integrity constraints
├── clean-database.sql                Drops all tables for a fresh start
└── troubleshooting/                  Diagnostic scripts used while developing the triggers
```

Layered architecture: **Controller → Service → DAO → Entity**. Controllers never touch the persistence layer directly.

## Getting started

### Requirements
- JDK 21
- PostgreSQL (16 or newer)

### 1. Create the database

```sql
CREATE DATABASE "School_Auto";
```

### 2. Configure the connection

The application reads `DB_URL`, `DB_USERNAME` and `DB_PASSWORD`. The defaults are `jdbc:postgresql://localhost:5432/School_Auto` and `postgres` / `postgres`.

You can set environment variables:

```bash
# Linux / macOS
export DB_PASSWORD=your_password
```

```powershell
# Windows PowerShell
$env:DB_PASSWORD = "your_password"
```

Alternatively, create a git-ignored `application-local.properties` file next to `pom.xml`:

```properties
spring.datasource.password=your_password
```

### 3. Run

```bash
./mvnw spring-boot:run        # Windows: mvnw.cmd spring-boot:run
```

Hibernate creates the tables on the first start. Then open **http://localhost:8080**.

### 4. (Optional) Load sample data and database logic

Run these scripts in psql or pgAdmin, after the first start:

```bash
psql -d School_Auto -f database/populate-database.sql
psql -d School_Auto -f database/add-database-constraints.sql
psql -d School_Auto -f database/database-triggers.sql
psql -d School_Auto -f database/classroom-average-procedure.sql
```

Sample logins from `populate-database.sql`:

| Where | Login | Password |
|---|---|---|
| Admin panel (`/admin/school-login`) | *Lincoln High School* | `lincoln123` |
| User login (`/persons/HomePage`) | first name `John` | `teacher123` |

## Main routes

| Route | Description |
|---|---|
| `/admin/school-login` | School admin login |
| `/admin/dashboard` | Dashboard with statistics |
| `/admin/showCatalog` | Catalogue of all management pages |
| `/admin/{entity}List` | List pages, e.g. `/admin/studentList`, `/admin/examList` |
| `/persons/HomePage` | User login |
| `/persons/register` | User registration |
| `/sse/messages/{conversationId}` | Live message stream (SSE) |

## Possible improvements

- Spring Security with role-based access instead of the custom session check
- Flyway migrations instead of `ddl-auto=update` and manual SQL scripts
- Integration tests with Testcontainers
- Login by e-mail instead of first name
- POST/DELETE instead of GET for destructive actions

## License

MIT
