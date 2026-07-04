package com.edgarrt.poc.paymentsimilarity.domain.model;

import java.util.List;
import java.util.UUID;

public record PaymentAssessment(
        UUID assessmentId,
        UUID paymentId,
        RiskAction action,
        double confidence,
        String reason,
        List<SimilarityMatch> matches
) {
    public PaymentAssessment {
        assessmentId = assessmentId == null ? UUID.randomUUID() : assessmentId;
        matches = matches == null ? List.of() : List.copyOf(matches);
        confidence = Math.max(0D, Math.min(confidence, 1D));
    }
}
