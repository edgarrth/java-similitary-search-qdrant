package com.edgarrt.poc.paymentsimilarity.domain.model;

import com.edgarrt.poc.paymentsimilarity.domain.exception.DomainException;

public record MerchantContext(
        String merchantId,
        String merchantName,
        String mcc,
        String country,
        String paymentMethod
) {
    public MerchantContext {
        merchantId = normalizeRequired(merchantId, "merchantId");
        merchantName = normalizeRequired(merchantName, "merchantName");
        mcc = normalizeRequired(mcc, "mcc");
        country = normalizeRequired(country, "country").toUpperCase();
        paymentMethod = normalizeRequired(paymentMethod, "paymentMethod").toUpperCase();
    }

    private static String normalizeRequired(String value, String field) {
        if (value == null || value.isBlank()) {
            throw new DomainException(field + " is required.");
        }
        return value.trim();
    }
}
