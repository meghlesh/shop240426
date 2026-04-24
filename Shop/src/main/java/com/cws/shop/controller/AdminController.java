package com.cws.shop.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cws.shop.dto.request.CreateAdminDto;
import com.cws.shop.dto.response.ApiResponse;
import com.cws.shop.dto.response.ProductResponseDto;
import com.cws.shop.model.ProductStatus;
import com.cws.shop.model.Role;
import com.cws.shop.model.User;
import com.cws.shop.repository.UserRepository;
import com.cws.shop.service.ProductService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/superadmin")
public class AdminController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ProductService productService;

    public AdminController(UserRepository userRepository, PasswordEncoder passwordEncoder, ProductService productService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.productService = productService;
    }

    @PostMapping("/create-admin")
    public ResponseEntity<ApiResponse<?>> createAdmin(@Valid @RequestBody CreateAdminDto dto){

        // Check duplicate email
        if(userRepository.existsByEmail(dto.getEmail())){
            return ResponseEntity.status(HttpStatus.CONFLICT)
            		.body(new ApiResponse<>(false,"Email already exists",null));
        }

        User user = new User();
        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        user.setMobileNumber(dto.getMobileNumber());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setRole(Role.ADMIN);
        user.setActive(true);

        userRepository.save(user);

        return ResponseEntity.status(HttpStatus.CREATED)
        		.body(new ApiResponse<>(true,"Admin created successfully",null));
    }
    
    // ✅ Approve Product (ONLY ADMIN)
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/products/{id}/approve")
    public ResponseEntity<ApiResponse<?>> approveProduct(@PathVariable Long id){

        productService.approveProduct(id);

        return ResponseEntity.ok(
                new ApiResponse<>(true, "Product approved", null)
        );
    }
    
    // ✅ Reject Product
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/products/{id}/reject")
    public ResponseEntity<ApiResponse<?>> rejectProduct(@PathVariable Long id){

        productService.rejectProduct(id);

        return ResponseEntity.ok(
                new ApiResponse<>(true, "Product rejected", null)
        );
    }
    
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/products/pending")
    public ResponseEntity<ApiResponse<List<ProductResponseDto>>> getPendingProducts(){

        return ResponseEntity.ok(
            new ApiResponse<>(true, "Pending products",
                productService.getProductsByStatus(ProductStatus.PENDING))
        );
    }
}

