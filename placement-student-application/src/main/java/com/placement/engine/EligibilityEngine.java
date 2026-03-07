package com.placement.engine;

import com.placement.domain.PlacementOffer;
import com.placement.domain.Student;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Rule-based eligibility engine that evaluates students against placement offers
 * using domain, specialization, credit requirements, and cumulative grades.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class EligibilityEngine {

    private final List<EligibilityRule> rules; // Spring auto-injects all EligibilityRule beans

    /**
     * Evaluates whether a student is eligible for a placement offer.
     * All rules must pass for the student to be eligible.
     */
    public EligibilityResult evaluate(Student student, PlacementOffer offer) {
        List<EligibilityRuleResult> ruleResults = new ArrayList<>();
        boolean allPassed = true;

        for (EligibilityRule rule : rules) {
            EligibilityRuleResult result = rule.evaluate(student, offer);
            ruleResults.add(result);
            if (!result.isPassed()) {
                allPassed = false;
            }
        }

        return EligibilityResult.builder()
                .eligible(allPassed)
                .studentId(student.getId())
                .studentName(student.getName())
                .offerId(offer.getId())
                .companyName(offer.getCompanyName())
                .ruleResults(ruleResults)
                .summary(allPassed ? "Student meets all eligibility criteria" : "Student does not meet one or more eligibility criteria")
                .build();
    }
}
