package com.example.order_service.repository;

import com.example.order_service.model.Order;  // FIXED: Import correct Order class
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    // JpaRepository provides all crud operation for order entity
}