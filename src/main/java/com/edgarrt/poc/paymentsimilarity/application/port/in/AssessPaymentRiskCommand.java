package com.edgarrt.poc.paymentsimilarity.application.port.in;

import com.edgarrt.poc.paymentsimilarity.domain.model.CurrencyCode;

import java.math.BigDecimal;
import java.util.UUID;

public record AssessPaymentRiskCommand(
        UUID paymentId,
        BigDecimal amount,
        CurrencyCode currency,
        String merchantId,
        String merchantName,
        String mcc,
        String country,
        String paymentMethod,
        String channel,
        boolean internationalCard,
        boolean newDevice,
        int previousChargebacks,
        int topK
) {
    public int normalizedTopK() {
        if (topK <= 0) {
            return 3;
        }
        return Math.min(topK, 10);
    }
}
