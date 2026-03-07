package com.placement.engine;

import com.placement.domain.PlacementOffer;
import com.placement.domain.Student;
import org.springframework.stereotype.Component;

/**
 * Rule: Student's domain must match the placement offer's required domain.
 * Uses case-insensitive comparison and supports partial matches (e.g., "CS" matches "Computer Science").
 */
@Component
public class DomainMatchRule implements EligibilityRule {

    @Override
    public String getRuleName() {
        return "Domain Match";
    }

    @Override
    public EligibilityRuleResult evaluate(Student student, PlacementOffer offer) {
        String studentDomain = student.getDomain() != null ? student.getDomain().trim().toLowerCase() : "";
        String requiredDomain = offer.getRequiredDomain() != null ? offer.getRequiredDomain().trim().toLowerCase() : "";

        if (requiredDomain.isEmpty()) {
            return EligibilityRuleResult.pass(getRuleName(), "No domain requirement specified");
        }

        if (studentDomain.isEmpty()) {
            return EligibilityRuleResult.fail(getRuleName(),
                    "Student has no domain specified");
        }

        // Exact match
        if (studentDomain.equals(requiredDomain)) {
            return EligibilityRuleResult.pass(getRuleName(),
                    "Domain '" + student.getDomain() + "' matches required '" + offer.getRequiredDomain() + "'");
        }

        // Partial match (student domain contains required or vice versa)
        if (studentDomain.contains(requiredDomain) || requiredDomain.contains(studentDomain)) {
            return EligibilityRuleResult.pass(getRuleName(),
                    "Domain '" + student.getDomain() + "' is compatible with '" + offer.getRequiredDomain() + "'");
        }

        return EligibilityRuleResult.fail(getRuleName(),
                "Domain '" + student.getDomain() + "' does not match required '" + offer.getRequiredDomain() + "'");
    }
}
