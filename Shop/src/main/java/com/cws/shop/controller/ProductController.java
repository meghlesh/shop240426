package com.cws.shop.controller;

import com.cws.shop.dto.request.CreateProductDto;
import com.cws.shop.dto.response.ApiResponse;
import com.cws.shop.dto.response.ProductResponseDto;
import com.cws.shop.model.ProductStatus;
import com.cws.shop.service.ProductService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService){
        this.productService = productService;
    }
    
    @PreAuthorize("hasRole('SELLER')")
    @PostMapping
    public ResponseEntity<ApiResponse<ProductResponseDto>> createProduct(
            @Valid @RequestBody CreateProductDto request){

        ProductResponseDto product = productService.createProduct(request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>(true, "Product created", product));
    }
    
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}/approve")
    public ResponseEntity<ApiResponse<?>> approveProduct(@PathVariable("id") Long id){

        String message = productService.approveProduct(id);

        return ResponseEntity.ok(
                new ApiResponse<>(true, message, null)
        );
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}/reject")
    public ResponseEntity<ApiResponse<?>> rejectProduct(@PathVariable("id") Long id){

        String message = productService.rejectProduct(id);

        return ResponseEntity.ok(
                new ApiResponse<>(true, message, null)
        );
    }
    
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/pending")
    public ResponseEntity<ApiResponse<List<ProductResponseDto>>> getPendingProducts(){

        return ResponseEntity.ok(
                new ApiResponse<>(true, "Pending products",
                        productService.getProductsByStatus(ProductStatus.PENDING))
        );
    }
    
    @GetMapping
    public ResponseEntity<ApiResponse<List<ProductResponseDto>>> getAllProducts(){

        List<ProductResponseDto> products = productService.getAllProducts();

        return ResponseEntity.ok(
                new ApiResponse<>(true, "Products fetched", products)
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ProductResponseDto>> getProduct(@PathVariable("id") Long id){

        return ResponseEntity.ok(
                new ApiResponse<>(true, "Product fetched", productService.getProductById(id))
        );
    }
    
    @PreAuthorize("hasRole('SELLER')")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ProductResponseDto>> updateProduct(
            @PathVariable Long id,
            @Valid @RequestBody CreateProductDto request){

        return ResponseEntity.ok(
                new ApiResponse<>(true, "Product updated",
                        productService.updateProduct(id, request))
        );
    }
    
    @PreAuthorize("hasRole('SELLER')")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> deleteProduct(@PathVariable("id") Long id){

        productService.deleteProduct(id);

        return ResponseEntity.ok(
                new ApiResponse<>(true, "Product deleted", null)
        );
    }
}