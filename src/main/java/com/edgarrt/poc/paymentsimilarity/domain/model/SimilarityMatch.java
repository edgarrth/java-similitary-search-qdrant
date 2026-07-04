package com.edgarrt.poc.paymentsimilarity.domain.model;

public record SimilarityMatch(
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
}
