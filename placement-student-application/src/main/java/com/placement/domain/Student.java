package com.placement.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "students")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Student name is required")
    @Column(nullable = false)
    private String name;

    @NotBlank(message = "Email is required")
    @Email
    @Column(nullable = false, unique = true)
    private String email;

    @NotBlank(message = "Domain is required (e.g., Computer Science, Electrical Engineering)")
    @Column(nullable = false)
    private String domain;

    @NotBlank(message = "Specialization is required (e.g., AI/ML, Software Development)")
    @Column(nullable = false)
    private String specialization;

    @NotNull(message = "Credits completed is required")
    @Min(0)
    @Column(nullable = false)
    private Integer creditsCompleted;

    @NotNull(message = "Cumulative grade/CGPA is required")
    @DecimalMin("0.0")
    @DecimalMax("10.0")
    @Column(nullable = false, precision = 4, scale = 2)
    private Double cumulativeGrade;

    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<CVMetadata> cvMetadataList = new ArrayList<>();

    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<PlacementApplication> applications = new ArrayList<>();

    public void addCVMetadata(CVMetadata cv) {
        cvMetadataList.add(cv);
        cv.setStudent(this);
    }
}
