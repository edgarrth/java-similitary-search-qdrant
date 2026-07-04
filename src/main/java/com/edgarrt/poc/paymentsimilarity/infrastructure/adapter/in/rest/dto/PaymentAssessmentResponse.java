package com.edgarrt.poc.paymentsimilarity.infrastructure.adapter.in.rest.dto;

import com.edgarrt.poc.paymentsimilarity.domain.model.PaymentAssessment;
import com.edgarrt.poc.paymentsimilarity.domain.model.RiskAction;

import java.util.List;
import java.util.UUID;

public record PaymentAssessmentResponse(
        UUID assessmentId,
        UUID paymentId,
        RiskAction action,
        double confidence,
        String reason,
        List<SimilarityMatchResponse> matches
) {
    public static PaymentAssessmentResponse from(PaymentAssessment assessment) {
        return new PaymentAssessmentResponse(
                assessment.assessmentId(),
                assessment.paymentId(),
                assessment.action(),
                assessment.confidence(),
                assessment.reason(),
                assessment.matches().stream().map(SimilarityMatchResponse::from).toList()
        );
    }
}
