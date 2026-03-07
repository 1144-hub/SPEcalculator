# Placement Student Application

A full-stack application for managing placement offers and student applications. Implements a **rule-based eligibility engine** that evaluates students against placement offers using domain, specialization, credit requirements, and cumulative grades. Supports **CV upload and metadata persistence**, linking student academic profiles with placement applications.

---

## Requirements Fulfilled

### 1. Rule-Based Eligibility Engine
- **Domain Match Rule**: Evaluates if the student's domain matches the placement offer's required domain.
- **Specialization Rule**: Evaluates if the student's specialization aligns with the offer (or passes if no specialization required).
- **Credit Requirement Rule**: Ensures the student has completed at least the minimum credits required.
- **Cumulative Grade Rule**: Ensures the student's CGPA meets or exceeds the minimum required.

All rules must pass for a student to be eligible. The engine returns detailed per-rule results.

### 2. CV Upload and Metadata Persistence
- CV upload (PDF, DOC, DOCX) for students.
- Metadata persistence (filename, file path, size, content type, upload time).
- CVs are linked to student academic profiles and can be attached to placement applications.
- Download endpoint for retrieving uploaded CVs.

### 3. Linking Academic Profiles with Applications
- Students have academic profiles (domain, specialization, credits, CGPA).
- Placement applications link students to offers and optionally to a CV.
- Eligibility is evaluated automatically when applying, and the result is persisted with the application.

---

## Tech Stack

- **Backend**: Java 17, Spring Boot 3.2
- **Database**: H2 (file-based, no setup required)
- **ORM**: Spring Data JPA / Hibernate
- **Frontend**: Vanilla HTML/CSS/JS (served by Spring Boot)

---

## How to Run

### Prerequisites
- Java 17+
- Maven 3.6+

### Build and Run
```bash
cd placement-student-application
mvn clean package
mvn spring-boot:run
```

Or run the JAR directly:
```bash
java -jar target/placement-student-application-1.0.0.jar
```

### Access
- **Web UI**: http://localhost:8080/
- **H2 Console** (development): http://localhost:8080/h2-console  
  - JDBC URL: `jdbc:h2:file:./data/placementdb`  
  - Username: `sa`  
  - Password: (empty)

---

## API Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET    | /api/students | List all students |
| POST   | /api/students | Create student |
| GET    | /api/students/{id} | Get student |
| PUT    | /api/students/{id} | Update student |
| DELETE | /api/students/{id} | Delete student |
| POST   | /api/students/{id}/cv | Upload CV |
| GET    | /api/students/{id}/cv | List CVs for student |
| GET    | /api/placement-offers | List all offers |
| POST   | /api/placement-offers | Create offer |
| GET    | /api/eligibility/check?studentId=&offerId= | Check eligibility |
| POST   | /api/applications | Submit application |
| GET    | /api/applications/student/{id} | List applications by student |
| GET    | /api/applications/offer/{id} | List applications by offer |

---

## Step-by-Step: How This Project Was Built

### Step 1: Project Setup
- Created Maven project with Spring Boot 3.2 parent.
- Added dependencies: Spring Web, Spring Data JPA, H2, Validation, Lombok.
- Created `PlacementStudentApplication` main class and `application.properties` with H2 and file upload config.

### Step 2: Domain Models
- **Student**: id, name, email, domain, specialization, creditsCompleted, cumulativeGrade. One-to-many with CVMetadata and PlacementApplication.
- **PlacementOffer**: id, companyName, requiredDomain, requiredSpecialization, minCreditsRequired, minCumulativeGradeRequired, description.
- **CVMetadata**: id, originalFileName, storedFileName, filePath, fileSizeBytes, contentType, uploadedAt. Many-to-one with Student.
- **PlacementApplication**: id, student, placementOffer, cvMetadata, status, appliedAt, eligibilityResult. Links student, offer, and optionally CV.
- **ApplicationStatus**: enum (SUBMITTED, UNDER_REVIEW, SHORTLISTED, SELECTED, REJECTED).

### Step 3: Rule-Based Eligibility Engine
- **EligibilityRule** interface: `evaluate(Student, PlacementOffer) → EligibilityRuleResult`.
- **EligibilityRuleResult**: ruleName, passed, reason.
- Implemented 4 rules:
  1. **DomainMatchRule**: Case-insensitive domain matching (exact or partial).
  2. **SpecializationRule**: Specialization match (passes if offer has no requirement).
  3. **CreditRequirementRule**: Student credits ≥ offer min credits.
  4. **CumulativeGradeRule**: Student CGPA ≥ offer min CGPA.
- **EligibilityEngine**: Injects all rules, evaluates sequentially. Returns `EligibilityResult` with overall eligibility and per-rule details.

### Step 4: CV Upload and Metadata Persistence
- **FileUploadConfig**: Creates upload directory on startup.
- **CVStorageService**: Validates file type (PDF, DOC, DOCX), stores file with UUID-based name, persists `CVMetadata` to DB. Links CV to student. Provides list and download methods.
- **CVMetadataRepository**: JPA repository for CV metadata.
- **CVController**: POST for upload, GET for list and download.

### Step 5: Application Service and Linking
- **PlacementApplicationService**: Orchestrates eligibility check and application submission.
- When applying: loads student and offer, runs eligibility engine, optionally links CV, persists application with eligibility result JSON.
- Repositories for Student, PlacementOffer, PlacementApplication.

### Step 6: REST API
- **StudentController**: CRUD for students.
- **PlacementOfferController**: CRUD for offers.
- **CVController**: Upload, list, download.
- **EligibilityController**: GET /eligibility/check with studentId and offerId.
- **PlacementApplicationController**: POST to apply, GET by student/offer.
- DTOs for request/response to avoid lazy-loading and circular references.

### Step 7: Frontend UI
- Single-page HTML with tabs: Students, Placement Offers, CV Upload, Check Eligibility, Applications.
- Forms to add students and offers, upload CVs, check eligibility, and submit applications.
- Fetches data from REST API, displays eligibility rule results and application lists.

### Step 8: Documentation
- README with requirements, tech stack, run instructions, API summary, and step-by-step build explanation.

---

## Project Structure

```
placement-student-application/
├── pom.xml
├── README.md
└── src/main/
    ├── java/com/placement/
    │   ├── PlacementStudentApplication.java
    │   ├── config/FileUploadConfig.java
    │   ├── controller/
    │   │   ├── StudentController.java
    │   │   ├── PlacementOfferController.java
    │   │   ├── CVController.java
    │   │   ├── EligibilityController.java
    │   │   └── PlacementApplicationController.java
    │   ├── domain/
    │   │   ├── Student.java
    │   │   ├── PlacementOffer.java
    │   │   ├── CVMetadata.java
    │   │   ├── PlacementApplication.java
    │   │   └── ApplicationStatus.java
    │   ├── dto/
    │   │   ├── StudentDto.java
    │   │   ├── PlacementOfferDto.java
    │   │   └── ApplicationResponseDto.java
    │   ├── engine/
    │   │   ├── EligibilityRule.java
    │   │   ├── EligibilityRuleResult.java
    │   │   ├── EligibilityResult.java
    │   │   ├── EligibilityEngine.java
    │   │   ├── DomainMatchRule.java
    │   │   ├── SpecializationRule.java
    │   │   ├── CreditRequirementRule.java
    │   │   └── CumulativeGradeRule.java
    │   ├── repository/
    │   │   ├── StudentRepository.java
    │   │   ├── PlacementOfferRepository.java
    │   │   ├── CVMetadataRepository.java
    │   │   └── PlacementApplicationRepository.java
    │   └── service/
    │       ├── CVStorageService.java
    │       └── PlacementApplicationService.java
    └── resources/
        ├── application.properties
        └── static/index.html
```

---

## License

MIT
