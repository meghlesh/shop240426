package com.cws.shop.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.cws.shop.dto.response.ApiResponse;
import com.cws.shop.dto.response.CartResponseDto;
import com.cws.shop.service.CartService;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @PreAuthorize("hasRole('BUYER')")
    @PostMapping("/add/{productId}")
    public ResponseEntity<ApiResponse<CartResponseDto>> addToCart(
            @PathVariable("productId") Long productId) {

        validateProductId(productId);

        CartResponseDto data = cartService.addToCart(productId);

        return ResponseEntity.ok(
                new ApiResponse<>(true, "Product added to cart", data)
        );
    }

    // GET CART
//    @PreAuthorize("hasRole('BUYER')")
    @GetMapping
    public ResponseEntity<ApiResponse<CartResponseDto>> getCart() {

        CartResponseDto data = cartService.getCart();

        return ResponseEntity.ok(
                new ApiResponse<>(true, "Cart fetched successfully", data)
        );
    }

    // REMOVE ITEM
    @PreAuthorize("hasRole('BUYER')")
    @DeleteMapping("/remove/{productId}")
    public ResponseEntity<ApiResponse<String>> removeItem(
            @PathVariable("productId")Long productId) {

        validateProductId(productId);

        cartService.removeItem(productId);

        return ResponseEntity.ok(
                new ApiResponse<>(true, "Item removed from cart", null)
        );
    }

    // VALIDATION METHOD
    private void validateProductId(Long productId) {
        if (productId == null || productId <= 0) {
            throw new IllegalArgumentException("Invalid product ID");
        }
    }
}