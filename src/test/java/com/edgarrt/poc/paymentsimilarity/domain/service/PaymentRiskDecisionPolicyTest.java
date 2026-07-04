package com.edgarrt.poc.paymentsimilarity.domain.service;

import com.edgarrt.poc.paymentsimilarity.domain.model.CurrencyCode;
import com.edgarrt.poc.paymentsimilarity.domain.model.MerchantContext;
import com.edgarrt.poc.paymentsimilarity.domain.model.Money;
import com.edgarrt.poc.paymentsimilarity.domain.model.PaymentIntent;
import com.edgarrt.poc.paymentsimilarity.domain.model.RiskAction;
import com.edgarrt.poc.paymentsimilarity.domain.model.RiskSignals;
import com.edgarrt.poc.paymentsimilarity.domain.model.SimilarityMatch;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class PaymentRiskDecisionPolicyTest {

    private final PaymentRiskDecisionPolicy policy = new PaymentRiskDecisionPolicy(0.78, 0.90, 0.78);

    @Test
    void shouldReviewWhenNoMatchesWereFound() {
        PaymentIntent payment = samplePayment(false, false, 0);

        var assessment = policy.decide(payment, List.of());

        assertThat(assessment.action()).isEqualTo(RiskAction.REVIEW);
        assertThat(assessment.confidence()).isZero();
    }

    @Test
    void shouldDeclineWhenThereIsStrongDeclineEvidence() {
        PaymentIntent payment = samplePayment(false, true, 1);
        SimilarityMatch match = new SimilarityMatch(
                "MRC-FRAUD-001",
                0.95,
                RiskAction.DECLINE,
                "known fraud pattern",
                "Bad Merchant",
                "5734",
                "PE",
                "CARD",
                800,
                420,
                "high risk"
        );

        var assessment = policy.decide(payment, List.of(match));

        assertThat(assessment.action()).isEqualTo(RiskAction.DECLINE);
        assertThat(assessment.confidence()).isEqualTo(0.95);
    }

    private PaymentIntent samplePayment(boolean internationalCard, boolean newDevice, int previousChargebacks) {
        return new PaymentIntent(
                UUID.randomUUID(),
                new Money(BigDecimal.valueOf(100), CurrencyCode.PEN),
                new MerchantContext("merchant-1", "Demo Merchant", "5734", "PE", "CARD"),
                new RiskSignals(internationalCard, newDevice, previousChargebacks, "ECOMMERCE"),
                Instant.now()
        );
    }
}
