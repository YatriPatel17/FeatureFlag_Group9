package com.example.order_service.client;

import com.example.order_service.dto.ProductResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

// Feign client for communicate with product service
@FeignClient(name = "product-service", url = "http://product-service:8081")
public interface ProductClient {

    // Call product service API to get the product detail by id
    @GetMapping("/api/products/{id}")
    ProductResponse getProductById(@PathVariable("id") Long id);
}