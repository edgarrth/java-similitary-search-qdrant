package com.edgarrt.poc.paymentsimilarity.application.port.in;

import com.edgarrt.poc.paymentsimilarity.domain.model.PaymentAssessment;

public interface AssessPaymentRiskUseCase {
    PaymentAssessment assess(AssessPaymentRiskCommand command);
}
