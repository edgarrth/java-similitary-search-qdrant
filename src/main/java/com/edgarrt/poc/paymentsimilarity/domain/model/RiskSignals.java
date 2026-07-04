package com.edgarrt.poc.paymentsimilarity.domain.model;

public record RiskSignals(
        boolean internationalCard,
        boolean newDevice,
        int previousChargebacks,
        String channel
) {
    public RiskSignals {
        previousChargebacks = Math.max(previousChargebacks, 0);
        channel = channel == null || channel.isBlank() ? "UNKNOWN" : channel.trim().toUpperCase();
    }
}
