package com.cws.shop.service;

import java.util.List;

import com.cws.shop.dto.request.CreateProductDto;
import com.cws.shop.dto.response.ProductResponseDto;
import com.cws.shop.model.ProductStatus;

public interface ProductService {
	ProductResponseDto createProduct(CreateProductDto dto);
	
	List<ProductResponseDto> getAllProducts();
	
	ProductResponseDto getProductById(Long id);
	
	ProductResponseDto updateProduct(Long id, CreateProductDto dto);
	
	void deleteProduct(Long id);
	
	String approveProduct(Long id);

    String rejectProduct(Long id);

	List<ProductResponseDto> getProductsByStatus(ProductStatus status);
}
