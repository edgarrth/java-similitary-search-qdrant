package com.edgarrt.poc.paymentsimilarity.domain.service;

import com.edgarrt.poc.paymentsimilarity.domain.model.PaymentAssessment;
import com.edgarrt.poc.paymentsimilarity.domain.model.PaymentIntent;
import com.edgarrt.poc.paymentsimilarity.domain.model.RiskAction;
import com.edgarrt.poc.paymentsimilarity.domain.model.SimilarityMatch;

import java.util.Comparator;
import java.util.List;
import java.util.UUID;

public class PaymentRiskDecisionPolicy {

    private final double minimumScoreForDecision;
    private final double declineScoreThreshold;
    private final double reviewScoreThreshold;

    public PaymentRiskDecisionPolicy(
            double minimumScoreForDecision,
            double declineScoreThreshold,
            double reviewScoreThreshold
    ) {
        this.minimumScoreForDecision = minimumScoreForDecision;
        this.declineScoreThreshold = declineScoreThreshold;
        this.reviewScoreThreshold = reviewScoreThreshold;
    }

    public PaymentAssessment decide(PaymentIntent payment, List<SimilarityMatch> matches) {
        if (matches == null || matches.isEmpty()) {
            return new PaymentAssessment(
                    UUID.randomUUID(),
                    payment.paymentId(),
                    RiskAction.REVIEW,
                    0D,
                    "No se encontraron perfiles similares en Qdrant; se envía a revisión preventiva.",
                    List.of()
            );
        }

        SimilarityMatch topMatch = matches.stream()
                .max(Comparator.comparingDouble(SimilarityMatch::score))
                .orElseThrow();

        if (topMatch.score() < minimumScoreForDecision) {
            return new PaymentAssessment(
                    UUID.randomUUID(),
                    payment.paymentId(),
                    RiskAction.REVIEW,
                    topMatch.score(),
                    "El mejor match está por debajo del umbral mínimo de decisión; se requiere revisión.",
                    matches
            );
        }

        RiskAction action = inferAction(topMatch, payment);
        return new PaymentAssessment(
                UUID.randomUUID(),
                payment.paymentId(),
                action,
                topMatch.score(),
                reasonFor(action, topMatch),
                matches
        );
    }

    private RiskAction inferAction(SimilarityMatch topMatch, PaymentIntent payment) {
        boolean hasStrongDeclineEvidence = topMatch.recommendedAction() == RiskAction.DECLINE
                && topMatch.score() >= declineScoreThreshold;
        boolean hasHighFraudSignal = topMatch.fraudRateBps() >= 250 && topMatch.score() >= reviewScoreThreshold;
        boolean hasDeviceRisk = payment.riskSignals().newDevice() && payment.riskSignals().previousChargebacks() > 0;

        if (hasStrongDeclineEvidence || (hasHighFraudSignal && hasDeviceRisk)) {
            return RiskAction.DECLINE;
        }

        if (topMatch.recommendedAction() == RiskAction.REVIEW || topMatch.score() >= reviewScoreThreshold) {
            return RiskAction.REVIEW;
        }

        return RiskAction.APPROVE;
    }

    private String reasonFor(RiskAction action, SimilarityMatch topMatch) {
        return switch (action) {
            case APPROVE -> "Perfil similar de bajo riesgo encontrado en Qdrant.";
            case REVIEW -> "Perfil similar de riesgo medio/alto encontrado en Qdrant.";
            case DECLINE -> "Perfil similar de alto riesgo encontrado en Qdrant con evidencia suficiente para rechazo.";
        };
    }
}
