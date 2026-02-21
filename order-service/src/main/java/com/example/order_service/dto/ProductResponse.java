package com.example.order_service.dto;

// DTO for product service response api
public class ProductResponse {

    // Product id
    private Long id;
    // Product name
    private String name;
    // Unit price
    private Double price;
    // Available quantity
    private Integer quantity;

    // Getter and setter method
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }
    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }


}

