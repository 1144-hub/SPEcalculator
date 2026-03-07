package com.placement.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudentDto {

    private Long id;

    @NotBlank(message = "Student name is required")
    private String name;

    @NotBlank(message = "Email is required")
    @Email
    private String email;

    @NotBlank(message = "Domain is required")
    private String domain;

    @NotBlank(message = "Specialization is required")
    private String specialization;

    @NotNull(message = "Credits completed is required")
    @Min(0)
    private Integer creditsCompleted;

    @NotNull(message = "Cumulative grade is required")
    @DecimalMin("0.0")
    @DecimalMax("10.0")
    private Double cumulativeGrade;
}
