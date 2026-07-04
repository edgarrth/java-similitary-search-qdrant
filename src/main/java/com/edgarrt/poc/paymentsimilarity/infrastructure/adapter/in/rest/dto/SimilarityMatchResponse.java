package com.edgarrt.poc.paymentsimilarity.infrastructure.adapter.in.rest.dto;

import com.edgarrt.poc.paymentsimilarity.domain.model.RiskAction;
import com.edgarrt.poc.paymentsimilarity.domain.model.SimilarityMatch;

public record SimilarityMatchResponse(
        String profileCode,
        double score,
        RiskAction recommendedAction,
        String label,
        String matchedMerchantName,
        String mcc,
        String country,
        String paymentMethod,
        int chargebackRateBps,
        int fraudRateBps,
        String notes
) {
    public static SimilarityMatchResponse from(SimilarityMatch match) {
        return new SimilarityMatchResponse(
                match.profileCode(),
                match.score(),
                match.recommendedAction(),
                match.label(),
                match.matchedMerchantName(),
                match.mcc(),
                match.country(),
                match.paymentMethod(),
                match.chargebackRateBps(),
                match.fraudRateBps(),
                match.notes()
        );
    }
}
