package com.example.order_service.controller;

import com.example.order_service.dto.OrderRequest;
import com.example.order_service.model.Order;
import com.example.order_service.service.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import tools.jackson.databind.ObjectMapper;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;

import java.util.Arrays;
import java.util.List;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class OrderControllerTest {

    private MockMvc mockMvc;

    @Mock
    private OrderService orderService;

    @InjectMocks
    private OrderController orderController;

    private ObjectMapper objectMapper = new ObjectMapper();

    private Order firstOrder;
    private Order secondOrder;
    private List<Order> orderList;
    private OrderRequest orderRequest;

    @BeforeEach
    public void setup(){
        mockMvc = MockMvcBuilders.standaloneSetup(orderController).build();

        firstOrder = new Order(1L, 2, 1200.0, "Pending");
        firstOrder.setId(1L);
        secondOrder = new Order(1L, 3, 1800.0, "Shipped");
        secondOrder.setId(2L);
        orderList = Arrays.asList(firstOrder,secondOrder);

        orderRequest = new OrderRequest();
        orderRequest.setProductId(1L);
        orderRequest.setQuantity(2);
        orderRequest.setStatus("Pending");
    }

    @Test
    void testGetAllOrders_ReturnsAllOrders() throws Exception {
        // Arrange
        when(orderService.getAllOrders()).thenReturn(orderList);

        // Act and Assert
        mockMvc.perform(get("/api/orders"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].totalPrice").value(1200.0))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].totalPrice").value(1800.0));

                verify(orderService, times(1)).getAllOrders();
    }

    @Test
    void testGetOrderById_ReturnsOrder() throws Exception {
        // Arrange
        when(orderService.getOrderById(1L)).thenReturn(firstOrder);
        // Act and Assert
        mockMvc.perform(get("/api/orders/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.totalPrice").value(1200.0));
        verify(orderService, times(1)).getOrderById(1L);
    }

    @Test
    void testGetOrderById_OrderDoesNotExist_ReturnsNotFound() throws Exception {
        when(orderService.getOrderById(99L)).thenReturn(null);

        mockMvc.perform(get("/api/orders/99"))
                        .andExpect(status().isNotFound());
        verify(orderService, times(1)).getOrderById(99L);
    }

    @Test
    void testCreateOrder_WithRuntimeException_ReturnsBadRequest() throws Exception {
        when(orderService.createOrder(any(OrderRequest.class)))
                .thenThrow(new RuntimeException("Product not found"));

        mockMvc.perform(post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(orderRequest)))
                .andExpect(status().isBadRequest());

        verify(orderService, times(1)).createOrder(any(OrderRequest.class));
    }
}
