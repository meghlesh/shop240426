package com.cws.shop.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cws.shop.dto.response.AdminDashboardDto;
import com.cws.shop.service.AdminDashboardService;

@RestController
@RequestMapping("/api/admin/dashboard")
public class AdminDashboardController {
    private final AdminDashboardService service;

    public AdminDashboardController(AdminDashboardService service) {
		this.service = service;
	}
    
    @PreAuthorize("hasRole('ADMIN')")
	@GetMapping("/dashboard-stats")
    public AdminDashboardDto  getDashboard() {
        return service.getDashboardData();
    }
}
