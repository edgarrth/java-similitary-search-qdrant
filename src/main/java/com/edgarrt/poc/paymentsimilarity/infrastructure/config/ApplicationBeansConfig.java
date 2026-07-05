package com.edgarrt.poc.paymentsimilarity.infrastructure.config;

import com.edgarrt.poc.paymentsimilarity.domain.service.PaymentRiskDecisionPolicy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestClient;

@Configuration
public class ApplicationBeansConfig {

    @Bean
    RestClient.Builder restClientBuilder() {
        return RestClient.builder();
    }

    @Bean
    RestClient qdrantRestClient(RestClient.Builder builder, QdrantProperties properties) {
        return builder
                .baseUrl(properties.url())
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

    @Bean
    PaymentRiskDecisionPolicy paymentRiskDecisionPolicy(RiskDecisionProperties properties) {
        return new PaymentRiskDecisionPolicy(
                properties.minimumScoreForDecision(),
                properties.declineScoreThreshold(),
                properties.reviewScoreThreshold()
        );
    }
}
