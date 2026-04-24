package com.cws.shop.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cws.shop.dto.response.SellerProductsData;
import com.cws.shop.service.SellerDashboardService;

@RestController
@RequestMapping("/api/seller")
public class SellerDashboardController {
//	@GetMapping("/dashboard")
//	public String dashboard(){
//	return "dashboard";
//	}
	
	private final SellerDashboardService sellerSashboardService;

	public SellerDashboardController(SellerDashboardService sellerDashboardService) {
	    this.sellerSashboardService = sellerDashboardService;
	}
	@PreAuthorize("hasRole('SELLER')")
    @GetMapping("/seller")
    public ResponseEntity<?> sellerDashboard() {
        return ResponseEntity.ok(sellerSashboardService.getSellerData());
    }
	
	@GetMapping("/dashboard-stats")
    public ResponseEntity<SellerProductsData> getDashboardStats(Authentication authentication) {
        
		if (authentication == null) {
	        throw new RuntimeException("User not authenticated");
	    }
		
        // 1. Get the email (subject) from the JWT token
        String email = authentication.getName();
        
        // 2. Fetch the calculated DTO from the service
        SellerProductsData stats = sellerSashboardService.getSellerDashboard(email);
        
        // 3. Return the response
        return ResponseEntity.ok(stats);
    }
}



