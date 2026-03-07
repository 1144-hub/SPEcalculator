package com.placement.engine;

import com.placement.domain.PlacementOffer;
import com.placement.domain.Student;
import org.springframework.stereotype.Component;

/**
 * Rule: Student must have completed at least the minimum credits required by the placement offer.
 */
@Component
public class CreditRequirementRule implements EligibilityRule {

    @Override
    public String getRuleName() {
        return "Credit Requirement";
    }

    @Override
    public EligibilityRuleResult evaluate(Student student, PlacementOffer offer) {
        Integer studentCredits = student.getCreditsCompleted();
        Integer minCredits = offer.getMinCreditsRequired();

        if (studentCredits == null) {
            return EligibilityRuleResult.fail(getRuleName(),
                    "Student has no credits recorded; minimum required: " + minCredits);
        }

        if (minCredits == null || minCredits <= 0) {
            return EligibilityRuleResult.pass(getRuleName(), "No minimum credit requirement");
        }

        if (studentCredits >= minCredits) {
            return EligibilityRuleResult.pass(getRuleName(),
                    "Credits " + studentCredits + " meets minimum " + minCredits + " required");
        }

        return EligibilityRuleResult.fail(getRuleName(),
                "Credits " + studentCredits + " below minimum " + minCredits + " required");
    }
}
