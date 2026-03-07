package com.placement.dto;

import com.placement.domain.ApplicationStatus;
import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApplicationResponseDto {

    private Long id;
    private Long studentId;
    private String studentName;
    private Long offerId;
    private String companyName;
    private Long cvMetadataId;
    private ApplicationStatus status;
    private LocalDateTime appliedAt;
    private String eligibilitySummary;
}
