package com.edgarrt.poc.paymentsimilarity.application.port.in;

import com.edgarrt.poc.paymentsimilarity.domain.model.MerchantRiskProfile;

public interface IndexMerchantRiskProfileUseCase {
    MerchantRiskProfile index(IndexMerchantRiskProfileCommand command);
}
