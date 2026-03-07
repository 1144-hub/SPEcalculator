package com.placement.controller;

import com.placement.domain.PlacementApplication;
import com.placement.dto.ApplicationResponseDto;
import com.placement.service.PlacementApplicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/applications")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class PlacementApplicationController {

    private final PlacementApplicationService applicationService;

    @PostMapping
    public ResponseEntity<?> apply(@RequestBody ApplyRequest request) {
        try {
            PlacementApplication application = applicationService.apply(
                    request.studentId(),
                    request.offerId(),
                    request.cvMetadataId()
            );
            return ResponseEntity.status(201).body(toDto(application));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/student/{studentId}")
    public List<ApplicationResponseDto> getApplicationsByStudent(@PathVariable Long studentId) {
        return applicationService.getApplicationsByStudent(studentId).stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/offer/{offerId}")
    public List<ApplicationResponseDto> getApplicationsByOffer(@PathVariable Long offerId) {
        return applicationService.getApplicationsByOffer(offerId).stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApplicationResponseDto> getApplication(@PathVariable Long id) {
        try {
            PlacementApplication application = applicationService.getApplicationById(id);
            return ResponseEntity.ok(toDto(application));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    private ApplicationResponseDto toDto(PlacementApplication a) {
        return ApplicationResponseDto.builder()
                .id(a.getId())
                .studentId(a.getStudent().getId())
                .studentName(a.getStudent().getName())
                .offerId(a.getPlacementOffer().getId())
                .companyName(a.getPlacementOffer().getCompanyName())
                .cvMetadataId(a.getCvMetadata() != null ? a.getCvMetadata().getId() : null)
                .status(a.getStatus())
                .appliedAt(a.getAppliedAt())
                .eligibilitySummary(a.getEligibilityResult())
                .build();
    }

    public record ApplyRequest(Long studentId, Long offerId, Long cvMetadataId) {}
}
