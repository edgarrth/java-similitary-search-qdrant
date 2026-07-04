package com.edgarrt.poc.paymentsimilarity;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class PaymentSimilarityApplication {

    public static void main(String[] args) {
        SpringApplication.run(PaymentSimilarityApplication.class, args);
    }
}
