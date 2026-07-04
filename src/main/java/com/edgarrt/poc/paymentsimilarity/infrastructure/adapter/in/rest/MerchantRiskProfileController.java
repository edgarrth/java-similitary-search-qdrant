package com.edgarrt.poc.paymentsimilarity.infrastructure.adapter.in.rest;

import com.edgarrt.poc.paymentsimilarity.application.port.in.IndexMerchantRiskProfileCommand;
import com.edgarrt.poc.paymentsimilarity.application.port.in.IndexMerchantRiskProfileUseCase;
import com.edgarrt.poc.paymentsimilarity.domain.model.RiskAction;
import com.edgarrt.poc.paymentsimilarity.infrastructure.adapter.in.rest.dto.IndexMerchantRiskProfileRequest;
import com.edgarrt.poc.paymentsimilarity.infrastructure.adapter.in.rest.dto.MerchantRiskProfileResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/merchant-risk-profiles")
public class MerchantRiskProfileController {

    private final IndexMerchantRiskProfileUseCase indexMerchantRiskProfileUseCase;

    public MerchantRiskProfileController(IndexMerchantRiskProfileUseCase indexMerchantRiskProfileUseCase) {
        this.indexMerchantRiskProfileUseCase = indexMerchantRiskProfileUseCase;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    MerchantRiskProfileResponse index(@Valid @RequestBody IndexMerchantRiskProfileRequest request) {
        var command = new IndexMerchantRiskProfileCommand(
                request.profileCode(),
                request.merchantName(),
                request.mcc(),
                request.country(),
                request.paymentMethod(),
                request.avgAmount(),
                request.chargebackRateBps(),
                request.fraudRateBps(),
                RiskAction.valueOf(request.recommendedAction().toUpperCase()),
                request.label(),
                request.notes()
        );
        return MerchantRiskProfileResponse.from(indexMerchantRiskProfileUseCase.index(command));
    }
}
