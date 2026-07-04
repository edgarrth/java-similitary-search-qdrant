package com.edgarrt.poc.paymentsimilarity.domain.model;

import com.edgarrt.poc.paymentsimilarity.domain.exception.DomainException;

import java.math.BigDecimal;
import java.math.RoundingMode;

public record Money(BigDecimal amount, CurrencyCode currency) {

    public Money {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new DomainException("Payment amount must be greater than zero.");
        }
        if (currency == null) {
            throw new DomainException("Currency is required.");
        }
        amount = amount.setScale(2, RoundingMode.HALF_UP);
    }

    public double normalizedAmount() {
        double capped = Math.min(amount.doubleValue(), 20_000D);
        return Math.log1p(capped) / Math.log1p(20_000D);
    }
}
