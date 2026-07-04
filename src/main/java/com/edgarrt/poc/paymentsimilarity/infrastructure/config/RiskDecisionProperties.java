package com.edgarrt.poc.paymentsimilarity.infrastructure.config;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "app.risk")
public record RiskDecisionProperties(
        @DecimalMin("0.0") @DecimalMax("1.0") double minimumScoreForDecision,
        @DecimalMin("0.0") @DecimalMax("1.0") double declineScoreThreshold,
        @DecimalMin("0.0") @DecimalMax("1.0") double reviewScoreThreshold
) {
}
