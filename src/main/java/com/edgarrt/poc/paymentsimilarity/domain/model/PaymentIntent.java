package com.edgarrt.poc.paymentsimilarity.domain.model;

import com.edgarrt.poc.paymentsimilarity.domain.exception.DomainException;

import java.time.Instant;
import java.util.UUID;

public record PaymentIntent(
        UUID paymentId,
        Money money,
        MerchantContext merchant,
        RiskSignals riskSignals,
        Instant occurredAt
) {
    public PaymentIntent {
        if (paymentId == null) {
            throw new DomainException("paymentId is required.");
        }
        if (money == null) {
            throw new DomainException("money is required.");
        }
        if (merchant == null) {
            throw new DomainException("merchant context is required.");
        }
        if (riskSignals == null) {
            riskSignals = new RiskSignals(false, false, 0, "UNKNOWN");
        }
        if (occurredAt == null) {
            occurredAt = Instant.now();
        }
    }
}
