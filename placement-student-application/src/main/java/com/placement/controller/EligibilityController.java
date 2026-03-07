package com.placement.controller;

import com.placement.engine.EligibilityResult;
import com.placement.service.PlacementApplicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/eligibility")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class EligibilityController {

    private final PlacementApplicationService applicationService;

    @GetMapping("/check")
    public ResponseEntity<?> checkEligibility(@RequestParam Long studentId,
                                              @RequestParam Long offerId) {
        try {
            EligibilityResult result = applicationService.checkEligibility(studentId, offerId);
            return ResponseEntity.ok(result);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
        }
    }

    public record ErrorResponse(String error) {}
}
