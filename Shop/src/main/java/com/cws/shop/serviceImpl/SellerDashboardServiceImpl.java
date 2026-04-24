package com.cws.shop.serviceImpl;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.cws.shop.dto.response.SellerProductsData;
import com.cws.shop.model.Product;
import com.cws.shop.model.ProductStatus;
import com.cws.shop.repository.ProductRepository;
import com.cws.shop.service.SellerDashboardService;

@Service
public class SellerDashboardServiceImpl implements SellerDashboardService{

	ProductRepository productRepository;
	
	public SellerDashboardServiceImpl(ProductRepository productRepository) {
		this.productRepository = productRepository;
	}

	@Override
	public Object getSellerData() {
		Map<String, Object> data = new HashMap<>();

//        data.put("totalSales", 100);
//        data.put("revenue", 12000);
//        data.put("activeProducts", 12);
//        data.put("pendingReviews", 3);

        return data;
	}

	@Override
	public SellerProductsData getSellerDashboard(String email) {
        // 1. Fetch the actual list of active products
        List<Product> activeProducts = productRepository.findByCreatedByEmailAndStatus(email, ProductStatus.APPROVED);
        
        // 2. Calculate Revenue from the list (using Java Stream)
        BigDecimal totalRevenue= new BigDecimal("0.00");
                

        // 3. Create the DTO
        SellerProductsData dto = new SellerProductsData();
        
        // Use .size() for the count - No extra query needed!
        dto.setActiveProducts(activeProducts.size()); 
        dto.setRevenue(totalRevenue);
        dto.setTotalSales(0);      // Set actual sales logic here later
        dto.setPendingReviews(0);

        return dto;
    }

}

