package com.placement.engine;

import com.placement.domain.PlacementOffer;
import com.placement.domain.Student;
import org.springframework.stereotype.Component;

/**
 * Rule: Student's specialization must match the placement offer's required specialization.
 * If offer has no specialization requirement, rule passes.
 */
@Component
public class SpecializationRule implements EligibilityRule {

    @Override
    public String getRuleName() {
        return "Specialization Match";
    }

    @Override
    public EligibilityRuleResult evaluate(Student student, PlacementOffer offer) {
        String requiredSpec = offer.getRequiredSpecialization();
        if (requiredSpec == null || requiredSpec.trim().isEmpty()) {
            return EligibilityRuleResult.pass(getRuleName(), "No specialization requirement specified");
        }

        String studentSpec = student.getSpecialization() != null ? student.getSpecialization().trim().toLowerCase() : "";
        String requiredSpecLower = requiredSpec.trim().toLowerCase();

        if (studentSpec.isEmpty()) {
            return EligibilityRuleResult.fail(getRuleName(),
                    "Student has no specialization; required: '" + offer.getRequiredSpecialization() + "'");
        }

        if (studentSpec.equals(requiredSpecLower)) {
            return EligibilityRuleResult.pass(getRuleName(),
                    "Specialization '" + student.getSpecialization() + "' matches required '" + offer.getRequiredSpecialization() + "'");
        }

        if (studentSpec.contains(requiredSpecLower) || requiredSpecLower.contains(studentSpec)) {
            return EligibilityRuleResult.pass(getRuleName(),
                    "Specialization '" + student.getSpecialization() + "' is compatible with '" + offer.getRequiredSpecialization() + "'");
        }

        return EligibilityRuleResult.fail(getRuleName(),
                "Specialization '" + student.getSpecialization() + "' does not match required '" + offer.getRequiredSpecialization() + "'");
    }
}
