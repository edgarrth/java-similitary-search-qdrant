package com.edgarrt.poc.paymentsimilarity.application.service;

import com.edgarrt.poc.paymentsimilarity.application.port.in.AssessPaymentRiskCommand;
import com.edgarrt.poc.paymentsimilarity.application.port.in.AssessPaymentRiskUseCase;
import com.edgarrt.poc.paymentsimilarity.application.port.out.MerchantRiskProfileVectorStorePort;
import com.edgarrt.poc.paymentsimilarity.application.port.out.PaymentEmbeddingPort;
import com.edgarrt.poc.paymentsimilarity.domain.model.MerchantContext;
import com.edgarrt.poc.paymentsimilarity.domain.model.Money;
import com.edgarrt.poc.paymentsimilarity.domain.model.PaymentAssessment;
import com.edgarrt.poc.paymentsimilarity.domain.model.PaymentIntent;
import com.edgarrt.poc.paymentsimilarity.domain.model.RiskSignals;
import com.edgarrt.poc.paymentsimilarity.domain.model.SimilarityMatch;
import com.edgarrt.poc.paymentsimilarity.domain.service.PaymentRiskDecisionPolicy;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
public class AssessPaymentRiskService implements AssessPaymentRiskUseCase {

    private final PaymentEmbeddingPort paymentEmbeddingPort;
    private final MerchantRiskProfileVectorStorePort vectorStorePort;
    private final PaymentRiskDecisionPolicy decisionPolicy;

    public AssessPaymentRiskService(
            PaymentEmbeddingPort paymentEmbeddingPort,
            MerchantRiskProfileVectorStorePort vectorStorePort,
            PaymentRiskDecisionPolicy decisionPolicy
    ) {
        this.paymentEmbeddingPort = paymentEmbeddingPort;
        this.vectorStorePort = vectorStorePort;
        this.decisionPolicy = decisionPolicy;
    }

    @Override
    public PaymentAssessment assess(AssessPaymentRiskCommand command) {
        PaymentIntent paymentIntent = new PaymentIntent(
                command.paymentId(),
                new Money(command.amount(), command.currency()),
                new MerchantContext(
                        command.merchantId(),
                        command.merchantName(),
                        command.mcc(),
                        command.country(),
                        command.paymentMethod()
                ),
                new RiskSignals(
                        command.internationalCard(),
                        command.newDevice(),
                        command.previousChargebacks(),
                        command.channel()
                ),
                Instant.now()
        );

        float[] embedding = paymentEmbeddingPort.embed(paymentIntent);
        List<SimilarityMatch> matches = vectorStorePort.search(embedding, command.normalizedTopK());
        return decisionPolicy.decide(paymentIntent, matches);
    }
}
