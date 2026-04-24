package com.cws.shop.service;

import com.cws.shop.dto.response.SellerProductsData;

public interface SellerDashboardService {
	Object getSellerData();
	
	public SellerProductsData getSellerDashboard(String email);
}


