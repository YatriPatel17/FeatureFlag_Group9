package com.example.order_service.dto;

// Requests from clients cor creating new orders
public class OrderRequest {

    // product id is required
    private Long productId;
    // order quantity is required
    private Integer quantity;
    // it is optional and default value is "Pending".
    private String status;

    // Getter and setter
    public Long getProductId() {
        return productId;
    }
    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Integer getQuantity() {
        return quantity;
    }
    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
}