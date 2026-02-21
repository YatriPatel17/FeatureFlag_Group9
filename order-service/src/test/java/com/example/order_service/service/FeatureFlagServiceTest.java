package com.example.order_service.service;

import io.getunleash.Unleash;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FeatureFlagServiceTest {

    @Mock
    private Unleash unleash;

    private FeatureFlagService featureFlagService;

    @BeforeEach
    public void setup() {
        featureFlagService = new FeatureFlagService(unleash);
    }

    @Test
    void testBulkOrderDiscountEnable_WhenFlagIsTrue_ReturnTrue(){
        // Arrange
        when(unleash.isEnabled("bulk-order-discount", false)).thenReturn(true);

        // Act
        boolean result = featureFlagService.isBulkOrderDiscountEnabled();

        // Assert
        assertTrue(result);
        verify(unleash, times(1)).isEnabled("bulk-order-discount", false);
    }

    @Test
    void testBulkOrderDiscountEnable_WhenFlagIsFalse_ReturnFalse() {
        //  Arrange
        when(unleash.isEnabled("bulk-order-discount", false)).thenReturn(false);

        // Act
        boolean result = featureFlagService.isBulkOrderDiscountEnabled();

        // Assert
        assertFalse(result);
        verify(unleash, times(1)).isEnabled("bulk-order-discount", false);
    }

    @Test
    void testBulkOrderDiscountEnable_WhenExceptionOccurs_ReturnFalse() {
        // Arrange
        when(unleash.isEnabled("bulk-order-discount", false)).thenThrow(new RuntimeException("Unleash unavailable"));
        // Act
        boolean result = featureFlagService.isBulkOrderDiscountEnabled();
        // Assert
        assertFalse(result);
        verify(unleash, times(1)).isEnabled("bulk-order-discount", false);
    }

    @Test
    void testOrderNotificationEnabled_WhenFlagIsTrue_ReturnTrue(){
        // Arrange
        when(unleash.isEnabled("order-notifications", false)).thenReturn(true);

        // Act
        boolean result = featureFlagService.isOrderNotificationsEnabled();

        // Assert
        assertTrue(result);
        verify(unleash, times(1)).isEnabled("order-notifications", false);
    }

    @Test
    void testOrderNotificationEnabled_WhenFlagIsFalse_ReturnFalse(){
        // Arrange
        when(unleash.isEnabled("order-notifications", false)).thenReturn(false);

        // Act
        boolean result = featureFlagService.isOrderNotificationsEnabled();

        // Assert
        assertFalse(result);
        verify(unleash, times(1)).isEnabled("order-notifications", false);
    }

    @Test
    void testOrderNotificationEnabled_ExceptionOccurs_ReturnFalse() {
        // Arrange
        when(unleash.isEnabled("order-notifications", false)).thenThrow(new RuntimeException("Unleash unavailable"));

        // Act
        boolean result = featureFlagService.isOrderNotificationsEnabled();

        // Assert
        assertFalse(result);
        verify(unleash, times(1)).isEnabled("order-notifications", false);
    }


}
