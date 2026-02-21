package com.example.order_service.service;

import com.example.order_service.client.ProductClient;
import com.example.order_service.dto.OrderRequest;
import com.example.order_service.dto.ProductResponse;
import com.example.order_service.model.Order;
import com.example.order_service.repository.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTests {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private ProductClient productClient;

    @Mock
    private FeatureFlagService featureFlagService;

    @InjectMocks
    private OrderService orderService;

    private Order order;
    private ProductResponse playStation5;
    private OrderRequest orderRequest;


    @BeforeEach
    void setUp() {
        playStation5 = new ProductResponse();
        playStation5.setId(1L);
        playStation5.setName("PlayStation5");
        playStation5.setPrice(600.0);
        playStation5.setQuantity(9);

        orderRequest = new OrderRequest();
        orderRequest.setProductId(1L);
        orderRequest.setQuantity(2);
        orderRequest.setStatus("Pending");

        order = new Order(1L, 2, 1200.0, "Pending");
    }

    @Test
    void testCreateOrder_WithRegularQuantity_CalculateCorrectPrice() {
        // Arrange
        when(productClient.getProductById(1L)).thenReturn(playStation5);
        when(featureFlagService.isBulkOrderDiscountEnabled()).thenReturn(false);
        when(featureFlagService.isOrderNotificationsEnabled()).thenReturn(false);
        when(orderRepository.save(any(Order.class))).thenAnswer(i -> i.getArgument(0));

        // Act
        Order result = orderService.createOrder(orderRequest);

        // Assert
        assertNotNull(result);
        assertEquals(1200.0, result.getTotalPrice());
        verify(featureFlagService, times(1)).isBulkOrderDiscountEnabled();

    }

    @Test
    void testCreateOrder_WithRegularQuantityAndFlagIsOn_CalculateCorrectPrice() {
        // Arrange
        orderRequest.setQuantity(6);
        when(productClient.getProductById(1L)).thenReturn(playStation5);
        when(featureFlagService.isBulkOrderDiscountEnabled()).thenReturn(true);
        when(featureFlagService.isOrderNotificationsEnabled()).thenReturn(false);
        when(orderRepository.save(any(Order.class))).thenAnswer(i -> i.getArgument(0));

        // Act
        Order result = orderService.createOrder(orderRequest);

        // Assert
        assertNotNull(result);
        assertEquals(3060.0, result.getTotalPrice(), 0.01);
        verify(featureFlagService, times(1)).isBulkOrderDiscountEnabled();

    }

    @Test
    void testCreateOrder_WithRegularQuantityAndFlagIsOff_CalculateCorrectPrice() {
        // Arrange
        orderRequest.setQuantity(6);
        when(productClient.getProductById(1L)).thenReturn(playStation5);
        when(featureFlagService.isBulkOrderDiscountEnabled()).thenReturn(false);
        when(featureFlagService.isOrderNotificationsEnabled()).thenReturn(false);
        when(orderRepository.save(any(Order.class))).thenAnswer(i -> i.getArgument(0));

        // Act
        Order result = orderService.createOrder(orderRequest);

        // Assert
        assertNotNull(result);
        assertEquals(3600.0, result.getTotalPrice(), 0.01);
        verify(featureFlagService, times(1)).isBulkOrderDiscountEnabled();
    }

    @Test
    void testCreateOrder_WithInsufficientStock_ThrowException(){
        playStation5.setQuantity(5);
        orderRequest.setQuantity(6);
        when(productClient.getProductById(1L)).thenReturn(playStation5);

        // Act and Assert
        RuntimeException runtimeException = assertThrows(RuntimeException.class,()->orderService.createOrder(orderRequest));
        assertTrue(runtimeException.getMessage().contains("Insufficient stock"));
        verify(orderRepository, never()).save(any(Order.class));
    }


}
