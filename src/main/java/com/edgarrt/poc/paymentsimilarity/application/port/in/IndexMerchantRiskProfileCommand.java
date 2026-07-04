package com.edgarrt.poc.paymentsimilarity.application.port.in;

import com.edgarrt.poc.paymentsimilarity.domain.model.RiskAction;

import java.math.BigDecimal;

public record IndexMerchantRiskProfileCommand(
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
}
