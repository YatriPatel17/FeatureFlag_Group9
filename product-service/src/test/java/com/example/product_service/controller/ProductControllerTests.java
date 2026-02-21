package com.example.product_service.controller;

import com.example.product_service.model.Product;
import com.example.product_service.service.FeatureFlagService;
import com.example.product_service.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import tools.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.List;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class ProductControllerTests {

    private MockMvc mockMvc;

    @Mock
    private ProductService productService;

    @Mock
    private FeatureFlagService featureFlagService;

    @InjectMocks
    private ProductController productController;

    private ObjectMapper objectMapper = new ObjectMapper();

    private Product playStation5;
    private Product desktop;
    private List<Product> productList;

    @BeforeEach
    public void setup() {
        playStation5 = new Product("PlayStation5", 600.0, 9);
        playStation5.setId(1L);

        desktop = new Product("Desktop", 250.0, 15);
        desktop.setId(2L);

        productList = new ArrayList<>();
        productList.add(playStation5);
        productList.add(desktop);

        mockMvc = MockMvcBuilders
                .standaloneSetup(productController)
                .build();
    }

    @Test
    void testGetAllProducts_ReturnAllProducts() throws Exception {
        // Arrange
        when(productService.getAllProducts()).thenReturn(productList);

        // Act and Assert
        mockMvc.perform(get("/api/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("PlayStation5"))
                .andExpect(jsonPath("$[0].price").value(600.0))
                .andExpect(jsonPath("$[1].name").value("Desktop"))
                .andExpect(jsonPath("$[1].price").value(250.0));

        verify(productService, times(1)).getAllProducts();
    }

    @Test
    void testGetProductById_ProductExists_ShouldReturnProduct() throws Exception {
        // Arrange
        when(productService.getProductById(1L)).thenReturn(playStation5);

        // Act and Assert
        mockMvc.perform(get("/api/products/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("PlayStation5"))
                .andExpect(jsonPath("$.price").value(600.0))
                .andExpect(jsonPath("$.quantity").value(9));

        verify(productService, times(1)).getProductById(1L);
    }

    @Test
    void testGetProductById_ProductDoesNotExist_ReturnNotFound() throws Exception {
        // Arrange
        when(productService.getProductById(99L)).thenReturn(null);

        // Act and Assert
        mockMvc.perform(get("/api/products/99"))
                .andExpect(status().isNotFound());

        verify(productService, times(1)).getProductById(99L);
    }

    @Test
    void testGetPremiumProducts_FlagIsOn_ReturnDiscountedPrices() throws Exception {
        // Arrange
        when(productService.getAllProducts()).thenReturn(productList);
        when(featureFlagService.isPremiumPricingEnabled()).thenReturn(true);

        // Act and Assert
        mockMvc.perform(get("/api/products/premium"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].price").value(540.0))
                .andExpect(jsonPath("$[1].price").value(225.0));

        verify(productService, times(1)).getAllProducts();
        verify(featureFlagService, times(1)).isPremiumPricingEnabled();
    }

    @Test
    void testGetPremiumProducts_FlagIsOff_ReturnRegularPrices() throws Exception {
        // Arrange
        when(productService.getAllProducts()).thenReturn(productList);
        when(featureFlagService.isPremiumPricingEnabled()).thenReturn(false);

        // Act and Assert
        mockMvc.perform(get("/api/products/premium"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].price").value(600.0))
                .andExpect(jsonPath("$[1].price").value(250.0));

        verify(productService, times(1)).getAllProducts();
        verify(featureFlagService, times(1)).isPremiumPricingEnabled();
    }


}