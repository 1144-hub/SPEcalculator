package com.placement.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "placement_applications")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlacementApplication {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "placement_offer_id", nullable = false)
    private PlacementOffer placementOffer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cv_metadata_id")
    private CVMetadata cvMetadata; // CV linked to this application

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private ApplicationStatus status = ApplicationStatus.SUBMITTED;

    @Column(nullable = false)
    private LocalDateTime appliedAt;

    private String eligibilityResult; // JSON or summary of rule evaluation
}
