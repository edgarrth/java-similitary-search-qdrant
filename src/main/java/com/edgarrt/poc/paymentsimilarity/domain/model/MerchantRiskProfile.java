package com.edgarrt.poc.paymentsimilarity.domain.model;

import com.edgarrt.poc.paymentsimilarity.domain.exception.DomainException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

public record MerchantRiskProfile(
        UUID profileId,
        String profileCode,
        String merchantName,
        String mcc,
        String country,
        String paymentMethod,
        BigDecimal avgAmount,
        int chargebackRateBps,
        int fraudRateBps,
        RiskAction recommendedAction,
        String label,
        String notes
) {
    public MerchantRiskProfile {
        profileCode = required(profileCode, "profileCode");
        merchantName = required(merchantName, "merchantName");
        mcc = required(mcc, "mcc");
        country = required(country, "country").toUpperCase();
        paymentMethod = required(paymentMethod, "paymentMethod").toUpperCase();
        if (avgAmount == null || avgAmount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new DomainException("avgAmount must be greater than zero.");
        }
        avgAmount = avgAmount.setScale(2, RoundingMode.HALF_UP);
        chargebackRateBps = Math.max(chargebackRateBps, 0);
        fraudRateBps = Math.max(fraudRateBps, 0);
        if (recommendedAction == null) {
            recommendedAction = RiskAction.REVIEW;
        }
        label = label == null ? "" : label.trim();
        notes = notes == null ? "" : notes.trim();
        if (profileId == null) {
            profileId = UUID.nameUUIDFromBytes(profileCode.getBytes(StandardCharsets.UTF_8));
        }
    }

    public double normalizedAverageAmount() {
        double capped = Math.min(avgAmount.doubleValue(), 20_000D);
        return Math.log1p(capped) / Math.log1p(20_000D);
    }

    private static String required(String value, String field) {
        if (value == null || value.isBlank()) {
            throw new DomainException(field + " is required.");
        }
        return value.trim();
    }
}
