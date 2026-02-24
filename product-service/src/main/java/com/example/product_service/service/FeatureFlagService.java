package com.example.product_service.service;

import io.getunleash.Unleash;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class FeatureFlagService {
    private static final Logger logger= LoggerFactory.getLogger(FeatureFlagService.class);
    private final Unleash unleash;

    // spring injects unleash client using constructor
    public FeatureFlagService(Unleash unleash) {
        this.unleash=unleash;
        logger.info("FeatureFlagService initialized for Product service");
    }

    // Checking if premium pricing feature flag is enabled
    public boolean isPremiumPricingEnabled(){
        try{
            return unleash.isEnabled("premium-pricing", false);
        }
        catch(Exception e){
            logger.error("Error checking premium-pricing flag", e);
            return false;
        }
    }
}
