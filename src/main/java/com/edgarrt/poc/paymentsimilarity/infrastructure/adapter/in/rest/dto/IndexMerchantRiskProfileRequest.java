package com.edgarrt.poc.paymentsimilarity.infrastructure.adapter.in.rest.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record IndexMerchantRiskProfileRequest(
        @NotBlank String profileCode,
        @NotBlank String merchantName,
        @NotBlank String mcc,
        @NotBlank String country,
        @NotBlank String paymentMethod,
        @NotNull @DecimalMin("0.01") BigDecimal avgAmount,
        @Min(0) int chargebackRateBps,
        @Min(0) int fraudRateBps,
        @NotBlank String recommendedAction,
        String label,
        String notes
) {
}
