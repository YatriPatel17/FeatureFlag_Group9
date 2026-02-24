package com.example.product_service.config;

import io.getunleash.DefaultUnleash;
import io.getunleash.Unleash;
import io.getunleash.util.UnleashConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UnleashConfigBean {

    // Configures the unleash client for feature flag
    @Bean
    public Unleash unleash(
        @Value("${unleash.api-url:http://localhost:4242/api}") String apiUrl,
        @Value("${unleash.app-name:product-service}") String appName,
        @Value("${unleash.instance-id:${HOSTNAME:local}}") String instanceId,
        @Value("${unleash.api-token:*:*.unleash-default-token}") String apiToken,
        @Value("${unleash.environment:development}") String environment
        ){
        // Build Unleash configuration
        UnleashConfig config = UnleashConfig.builder()
                .unleashAPI(apiUrl)
                .appName(appName)
                .instanceId(instanceId)
                .apiKey(apiToken)
                .environment(environment)
                .build();

        // Create & return Unleash client
        return new DefaultUnleash(config);
    }

}
