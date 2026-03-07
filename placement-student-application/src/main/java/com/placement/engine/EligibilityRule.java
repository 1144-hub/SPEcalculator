package com.placement.engine;

import com.placement.domain.Student;
import com.placement.domain.PlacementOffer;

/**
 * Interface for eligibility rules that evaluate whether a student qualifies for a placement offer.
 */
public interface EligibilityRule {

    String getRuleName();

    /**
     * Evaluates the rule and returns the result.
     * @return EligibilityRuleResult with pass/fail and reason
     */
    EligibilityRuleResult evaluate(Student student, PlacementOffer offer);
}
