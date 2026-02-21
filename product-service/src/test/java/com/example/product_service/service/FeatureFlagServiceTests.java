package com.example.product_service.service;

import io.getunleash.Unleash;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FeatureFlagServiceTests {

    @Mock
    private Unleash unleash;

    private FeatureFlagService featureFlagService;

    @BeforeEach
    void setUp() {
        featureFlagService = new FeatureFlagService(unleash);
    }

	@Test
	void testPremiumPricingEnabled_FlagISTrue_ShouldReturnTrue() {
        // Arrange
        when(unleash.isEnabled("premium-pricing", false)).thenReturn(true);

        // Act
        boolean result = featureFlagService.isPremiumPricingEnabled();

        // Assert
        assertTrue(result);
        verify(unleash, times(1)).isEnabled("premium-pricing", false);
	}

    @Test
    void testPremiumPricingEnabled_FlagISFalse_ShouldReturnFalse() {
        // Arrange
        when(unleash.isEnabled("premium-pricing", false)).thenReturn(false);

        //Act
        boolean result = featureFlagService.isPremiumPricingEnabled();

        // Arrange
        assertFalse(result);
        verify(unleash, times(1)).isEnabled("premium-pricing", false);
    }

    @Test
    void testPremiumPricingEnabled_ExceptionOccurs_ShouldReturnTrue() {
        // Arrange
        when(unleash.isEnabled("premium-pricing", false)).thenThrow(new RuntimeException("Unleash unavailable!"));

        //Act
        boolean result = featureFlagService.isPremiumPricingEnabled();

        //Assert
        assertFalse(result);
        verify(unleash, times(1)).isEnabled("premium-pricing", false);
    }

    @Test
    void testWithNullUnleash_ReturnFalse(){
        //Arrange
        FeatureFlagService serviceNull = new FeatureFlagService(null);

        // Act
        boolean result = serviceNull.isPremiumPricingEnabled();

        // Assert
        assertFalse(result);
    }
}
