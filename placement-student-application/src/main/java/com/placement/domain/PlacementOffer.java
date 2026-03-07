package com.placement.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "placement_offers")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlacementOffer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Company name is required")
    @Column(nullable = false)
    private String companyName;

    @NotBlank(message = "Required domain is required")
    @Column(nullable = false, name = "required_domain")
    private String requiredDomain;

    @Column(name = "required_specialization")
    private String requiredSpecialization; // Optional - null means any specialization

    @NotNull(message = "Minimum credits required")
    @Min(0)
    @Column(nullable = false, name = "min_credits")
    private Integer minCreditsRequired;

    @NotNull(message = "Minimum cumulative grade/CGPA required")
    @DecimalMin("0.0")
    @DecimalMax("10.0")
    @Column(nullable = false, name = "min_cumulative_grade", precision = 4, scale = 2)
    private Double minCumulativeGradeRequired;

    @Column(columnDefinition = "TEXT")
    private String description;

    @OneToMany(mappedBy = "placementOffer", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<PlacementApplication> applications = new ArrayList<>();
}
