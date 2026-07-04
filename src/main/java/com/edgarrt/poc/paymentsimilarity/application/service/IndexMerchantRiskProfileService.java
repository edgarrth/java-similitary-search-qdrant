package com.edgarrt.poc.paymentsimilarity.application.service;

import com.edgarrt.poc.paymentsimilarity.application.port.in.IndexMerchantRiskProfileCommand;
import com.edgarrt.poc.paymentsimilarity.application.port.in.IndexMerchantRiskProfileUseCase;
import com.edgarrt.poc.paymentsimilarity.application.port.out.MerchantRiskProfileVectorStorePort;
import com.edgarrt.poc.paymentsimilarity.application.port.out.PaymentEmbeddingPort;
import com.edgarrt.poc.paymentsimilarity.domain.model.MerchantRiskProfile;
import org.springframework.stereotype.Service;

@Service
public class IndexMerchantRiskProfileService implements IndexMerchantRiskProfileUseCase {

    private final PaymentEmbeddingPort paymentEmbeddingPort;
    private final MerchantRiskProfileVectorStorePort vectorStorePort;

    public IndexMerchantRiskProfileService(
            PaymentEmbeddingPort paymentEmbeddingPort,
            MerchantRiskProfileVectorStorePort vectorStorePort
    ) {
        this.paymentEmbeddingPort = paymentEmbeddingPort;
        this.vectorStorePort = vectorStorePort;
    }

    @Override
    public MerchantRiskProfile index(IndexMerchantRiskProfileCommand command) {
        MerchantRiskProfile profile = new MerchantRiskProfile(
                null,
                command.profileCode(),
                command.merchantName(),
                command.mcc(),
                command.country(),
                command.paymentMethod(),
                command.avgAmount(),
                command.chargebackRateBps(),
                command.fraudRateBps(),
                command.recommendedAction(),
                command.label(),
                command.notes()
        );
        float[] embedding = paymentEmbeddingPort.embed(profile);
        vectorStorePort.upsert(profile, embedding);
        return profile;
    }
}
