package com.placement.engine;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EligibilityResult {

    private boolean eligible;
    private Long studentId;
    private String studentName;
    private Long offerId;
    private String companyName;
    private List<EligibilityRuleResult> ruleResults;
    private String summary;
}
