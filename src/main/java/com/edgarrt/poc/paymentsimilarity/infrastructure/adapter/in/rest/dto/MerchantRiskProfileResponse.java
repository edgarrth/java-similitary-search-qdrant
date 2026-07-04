package com.edgarrt.poc.paymentsimilarity.infrastructure.adapter.in.rest.dto;

import com.edgarrt.poc.paymentsimilarity.domain.model.MerchantRiskProfile;
import com.edgarrt.poc.paymentsimilarity.domain.model.RiskAction;

import java.math.BigDecimal;
import java.util.UUID;

public record MerchantRiskProfileResponse(
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
    public static MerchantRiskProfileResponse from(MerchantRiskProfile profile) {
        return new MerchantRiskProfileResponse(
                profile.profileId(),
                profile.profileCode(),
                profile.merchantName(),
                profile.mcc(),
                profile.country(),
                profile.paymentMethod(),
                profile.avgAmount(),
                profile.chargebackRateBps(),
                profile.fraudRateBps(),
                profile.recommendedAction(),
                profile.label(),
                profile.notes()
        );
    }
}
