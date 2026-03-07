package com.placement.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.placement.domain.*;
import com.placement.engine.EligibilityEngine;
import com.placement.engine.EligibilityResult;
import com.placement.repository.PlacementApplicationRepository;
import com.placement.repository.PlacementOfferRepository;
import com.placement.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Service that links student academic profiles with placement applications.
 * Uses the eligibility engine to evaluate applications and persists CV metadata.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class PlacementApplicationService {

    private final StudentRepository studentRepository;
    private final PlacementOfferRepository offerRepository;
    private final PlacementApplicationRepository applicationRepository;
    private final EligibilityEngine eligibilityEngine;
    private final ObjectMapper objectMapper;

    public EligibilityResult checkEligibility(Long studentId, Long offerId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new IllegalArgumentException("Student not found: " + studentId));
        PlacementOffer offer = offerRepository.findById(offerId)
                .orElseThrow(() -> new IllegalArgumentException("Placement offer not found: " + offerId));

        return eligibilityEngine.evaluate(student, offer);
    }

    @Transactional
    public PlacementApplication apply(Long studentId, Long offerId, Long cvMetadataId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new IllegalArgumentException("Student not found: " + studentId));
        PlacementOffer offer = offerRepository.findById(offerId)
                .orElseThrow(() -> new IllegalArgumentException("Placement offer not found: " + offerId));

        if (applicationRepository.existsByStudentIdAndPlacementOfferId(studentId, offerId)) {
            throw new IllegalArgumentException("Application already exists for this offer");
        }

        EligibilityResult eligibility = eligibilityEngine.evaluate(student, offer);
        String eligibilityJson = toJson(eligibility);

        CVMetadata cvMetadata = null;
        if (cvMetadataId != null) {
            cvMetadata = student.getCvMetadataList().stream()
                    .filter(cv -> cv.getId().equals(cvMetadataId))
                    .findFirst()
                    .orElse(null);
        }

        PlacementApplication application = PlacementApplication.builder()
                .student(student)
                .placementOffer(offer)
                .cvMetadata(cvMetadata)
                .status(ApplicationStatus.SUBMITTED)
                .appliedAt(LocalDateTime.now())
                .eligibilityResult(eligibilityJson)
                .build();

        return applicationRepository.save(application);
    }

    public List<PlacementApplication> getApplicationsByStudent(Long studentId) {
        return applicationRepository.findByStudentId(studentId);
    }

    public List<PlacementApplication> getApplicationsByOffer(Long offerId) {
        return applicationRepository.findByPlacementOfferId(offerId);
    }

    public PlacementApplication getApplicationById(Long id) {
        return applicationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Application not found: " + id));
    }

    private String toJson(EligibilityResult result) {
        try {
            return objectMapper.writeValueAsString(result);
        } catch (JsonProcessingException e) {
            log.warn("Could not serialize eligibility result", e);
            return result.getSummary();
        }
    }
}
