package com.edgarrt.poc.paymentsimilarity.infrastructure.adapter.in.rest.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.UUID;

public record AssessPaymentRiskRequest(
        @NotNull UUID paymentId,
        @NotNull @DecimalMin("0.01") BigDecimal amount,
        @NotBlank String currency,
        @NotBlank String merchantId,
        @NotBlank String merchantName,
        @NotBlank String mcc,
        @NotBlank String country,
        @NotBlank String paymentMethod,
        @NotBlank String channel,
        boolean internationalCard,
        boolean newDevice,
        @Min(0) int previousChargebacks,
        @Min(1) @Max(10) int topK
) {
}
