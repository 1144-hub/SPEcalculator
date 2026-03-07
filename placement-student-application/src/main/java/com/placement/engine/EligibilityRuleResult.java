package com.placement.engine;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EligibilityRuleResult {

    private String ruleName;
    private boolean passed;
    private String reason;

    public static EligibilityRuleResult pass(String ruleName, String reason) {
        return EligibilityRuleResult.builder()
                .ruleName(ruleName)
                .passed(true)
                .reason(reason)
                .build();
    }

    public static EligibilityRuleResult fail(String ruleName, String reason) {
        return EligibilityRuleResult.builder()
                .ruleName(ruleName)
                .passed(false)
                .reason(reason)
                .build();
    }
}
