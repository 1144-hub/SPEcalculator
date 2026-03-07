package com.placement.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlacementOfferDto {

    private Long id;

    @NotBlank(message = "Company name is required")
    private String companyName;

    @NotBlank(message = "Required domain is required")
    private String requiredDomain;

    private String requiredSpecialization;

    @NotNull(message = "Minimum credits required")
    @Min(0)
    private Integer minCreditsRequired;

    @NotNull(message = "Minimum cumulative grade required")
    @DecimalMin("0.0")
    @DecimalMax("10.0")
    private Double minCumulativeGradeRequired;

    private String description;
}
