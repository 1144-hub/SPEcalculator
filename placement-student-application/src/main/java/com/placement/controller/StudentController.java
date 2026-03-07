package com.placement.controller;

import com.placement.domain.Student;
import com.placement.dto.StudentDto;
import com.placement.repository.StudentRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/students")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class StudentController {

    private final StudentRepository studentRepository;

    @GetMapping
    public List<StudentDto> getAllStudents() {
        return studentRepository.findAll().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<StudentDto> getStudent(@PathVariable Long id) {
        return studentRepository.findById(id)
                .map(s -> ResponseEntity.ok(toDto(s)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> createStudent(@Valid @RequestBody StudentDto dto) {
        if (studentRepository.existsByEmail(dto.getEmail())) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Student with email " + dto.getEmail() + " already exists");
        }
        Student student = toEntity(dto);
        student = studentRepository.save(student);
        return ResponseEntity.status(HttpStatus.CREATED).body(toDto(student));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateStudent(@PathVariable Long id, @Valid @RequestBody StudentDto dto) {
        return studentRepository.findById(id)
                .map(s -> {
                    s.setName(dto.getName());
                    s.setEmail(dto.getEmail());
                    s.setDomain(dto.getDomain());
                    s.setSpecialization(dto.getSpecialization());
                    s.setCreditsCompleted(dto.getCreditsCompleted());
                    s.setCumulativeGrade(dto.getCumulativeGrade());
                    return ResponseEntity.ok(toDto(studentRepository.save(s)));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStudent(@PathVariable Long id) {
        if (studentRepository.existsById(id)) {
            studentRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    private StudentDto toDto(Student s) {
        return StudentDto.builder()
                .id(s.getId())
                .name(s.getName())
                .email(s.getEmail())
                .domain(s.getDomain())
                .specialization(s.getSpecialization())
                .creditsCompleted(s.getCreditsCompleted())
                .cumulativeGrade(s.getCumulativeGrade())
                .build();
    }

    private Student toEntity(StudentDto dto) {
        return Student.builder()
                .name(dto.getName())
                .email(dto.getEmail())
                .domain(dto.getDomain())
                .specialization(dto.getSpecialization())
                .creditsCompleted(dto.getCreditsCompleted())
                .cumulativeGrade(dto.getCumulativeGrade())
                .build();
    }
}
