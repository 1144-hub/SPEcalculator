package com.placement.engine;

import com.placement.domain.PlacementOffer;
import com.placement.domain.Student;
import org.springframework.stereotype.Component;

/**
 * Rule: Student's cumulative grade/CGPA must meet or exceed the minimum required by the placement offer.
 */
@Component
public class CumulativeGradeRule implements EligibilityRule {

    @Override
    public String getRuleName() {
        return "Cumulative Grade (CGPA)";
    }

    @Override
    public EligibilityRuleResult evaluate(Student student, PlacementOffer offer) {
        Double studentGrade = student.getCumulativeGrade();
        Double minGrade = offer.getMinCumulativeGradeRequired();

        if (studentGrade == null) {
            return EligibilityRuleResult.fail(getRuleName(),
                    "Student has no cumulative grade; minimum required: " + minGrade);
        }

        if (minGrade == null || minGrade <= 0) {
            return EligibilityRuleResult.pass(getRuleName(), "No minimum grade requirement");
        }

        if (studentGrade >= minGrade) {
            return EligibilityRuleResult.pass(getRuleName(),
                    "CGPA " + studentGrade + " meets minimum " + minGrade + " required");
        }

        return EligibilityRuleResult.fail(getRuleName(),
                "CGPA " + studentGrade + " below minimum " + minGrade + " required");
    }
}
