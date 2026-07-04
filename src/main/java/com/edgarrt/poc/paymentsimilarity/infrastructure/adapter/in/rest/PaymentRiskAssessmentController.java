package com.edgarrt.poc.paymentsimilarity.infrastructure.adapter.in.rest;

import com.edgarrt.poc.paymentsimilarity.application.port.in.AssessPaymentRiskCommand;
import com.edgarrt.poc.paymentsimilarity.application.port.in.AssessPaymentRiskUseCase;
import com.edgarrt.poc.paymentsimilarity.domain.model.CurrencyCode;
import com.edgarrt.poc.paymentsimilarity.infrastructure.adapter.in.rest.dto.AssessPaymentRiskRequest;
import com.edgarrt.poc.paymentsimilarity.infrastructure.adapter.in.rest.dto.PaymentAssessmentResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/payment-risk-assessments")
public class PaymentRiskAssessmentController {

    private final AssessPaymentRiskUseCase assessPaymentRiskUseCase;

    public PaymentRiskAssessmentController(AssessPaymentRiskUseCase assessPaymentRiskUseCase) {
        this.assessPaymentRiskUseCase = assessPaymentRiskUseCase;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    PaymentAssessmentResponse assess(@Valid @RequestBody AssessPaymentRiskRequest request) {
        var command = new AssessPaymentRiskCommand(
                request.paymentId(),
                request.amount(),
                CurrencyCode.valueOf(request.currency().toUpperCase()),
                request.merchantId(),
                request.merchantName(),
                request.mcc(),
                request.country(),
                request.paymentMethod(),
                request.channel(),
                request.internationalCard(),
                request.newDevice(),
                request.previousChargebacks(),
                request.topK()
        );
        return PaymentAssessmentResponse.from(assessPaymentRiskUseCase.assess(command));
    }
}
