package com.example.product_service.controller;

import com.example.product_service.service.FeatureFlagService;
import com.example.product_service.model.Product;
import com.example.product_service.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")  // base url for all endpoints
public class ProductController {

    // Injecting product service dependency
    @Autowired
    private ProductService productService;

    @Autowired
    private FeatureFlagService featureFlagService;

    // Get all products
    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts() {
        List<Product> products = productService.getAllProducts();
        return ResponseEntity.ok(products);
    }

    // Get single product by ID
    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Long id) {
        Product product = productService.getProductById(id);
        if (product == null) {
            return ResponseEntity.notFound().build();
        }
        else  {
            return ResponseEntity.ok(product);
        }
    }

    @GetMapping("/premium")
    public ResponseEntity<List<Product>> getPremiumProducts() {
        List<Product> products = productService.getAllProducts();

        boolean isEnabled = featureFlagService.isPremiumPricingEnabled();
        System.out.println("Premium pricing flag is: " + isEnabled);

        if(isEnabled){
            // Applying 10% discount when flag is ON
            products.forEach(product -> {
                double discountPrice = product.getPrice() * 0.9;
                product.setPrice(Math.round(discountPrice * 100.0) / 100.0);
            });
        }
        return ResponseEntity.ok(products);
    }

    // Create a new product
    @PostMapping
    public ResponseEntity<Product> createProduct(@RequestBody Product product) {
        Product createdProduct = productService.createProduct(product);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdProduct);
    }

    // Delete a product by id
    @DeleteMapping("/{id}")
    public ResponseEntity<Product> deleteProductById(@PathVariable Long id) {
        Product product = productService.getProductById(id);
        if (product == null) {
            return ResponseEntity.notFound().build();
        }
        else {
            productService.deleteProduct(id);
            return ResponseEntity.noContent().build();
        }
    }

}
