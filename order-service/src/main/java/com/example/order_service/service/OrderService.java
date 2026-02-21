package com.example.order_service.service;

import com.example.order_service.client.ProductClient;
import com.example.order_service.dto.OrderRequest;
import com.example.order_service.dto.ProductResponse;
import com.example.order_service.model.Order;
import com.example.order_service.repository.OrderRepository;
import feign.FeignException;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OrderService {

    private  static final Logger logger = LoggerFactory.getLogger(OrderService.class);

    @Autowired
    private OrderRepository orderRepository;

    // Calling product service
    @Autowired
    private ProductClient productClient;

    @Autowired
    private FeatureFlagService  featureFlagService;

    // Getting all orders using list
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    // finds order by id
    public Order getOrderById(Long id) {
        Optional<Order> order = orderRepository.findById(id);
        // if order null it returns null
        return order.orElse(null);
    }

    // Creating new order
    @Transactional
    public Order createOrder(OrderRequest orderRequest) {
        try {
            System.out.println("Creating order for productId: " + orderRequest.getProductId());

            // Get product detail from product service
            ProductResponse product = productClient.getProductById(orderRequest.getProductId());

            System.out.println("Product response: " + product);

            // checking product is available or not
            if (product == null) {
                throw new RuntimeException("Product not found with id: " + orderRequest.getProductId());
            }

            System.out.println("Product found: " + product.getName() +
                    ", Price: " + product.getPrice() +
                    ", Quantity: " + product.getQuantity());

            // Checking product quantity is available
            if (product.getQuantity() < orderRequest.getQuantity()) {
                throw new RuntimeException(
                        "Insufficient stock. Available: " + product.getQuantity() +
                                ", Requested: " + orderRequest.getQuantity()
                );
            }

            // Calculating total price
            double totalPrice = product.getPrice() * orderRequest.getQuantity();

           // Bulk order discount
            boolean bulkDiscountEnabled = featureFlagService.isBulkOrderDiscountEnabled();
            boolean isBulkOrder = orderRequest.getQuantity() > 5;

            if (bulkDiscountEnabled && isBulkOrder) {
                totalPrice = totalPrice * 0.85;
                logger.info("Bulk order discount enabled with {} items", orderRequest.getQuantity());
            }

            // set default status
            String status = orderRequest.getStatus();
            if (status == null || status.trim().isEmpty()) {
                status = "Pending";
            }

            // Create and save order
            Order order = new Order(
                    orderRequest.getProductId(),
                    orderRequest.getQuantity(),
                    totalPrice,
                    status
            );

            Order savedOrder = orderRepository.save(order);
            //System.out.println("Order saved successfully: " + savedOrder.getId());
            logger.info("Order saved successfully: {} ", savedOrder.getId());
            if(featureFlagService.isOrderNotificationsEnabled()){
                logger.info("==========Order Notification=============");
                logger.info("Order ID: {}", savedOrder.getId());
                logger.info("Product: {} (ID: {})", product.getName(), product.getId());
                logger.info("Quality: {}", orderRequest.getQuantity());
                logger.info("Total price: ${}", String.format("%.2f", savedOrder.getTotalPrice()));
                logger.info("Status: {}", savedOrder.getStatus());
                logger.info("==========================================");
            }
            return savedOrder;

        } catch (FeignException e) {
            logger.error("FeignException: {}", e.getMessage());
            throw new RuntimeException("Error calling product service: " + e.getMessage());
        } catch (Exception e) {
            logger.error("Exception in createOrder: {}", e.getMessage());
            throw new RuntimeException("Error creating order: " + e.getMessage());
        }
    }
}