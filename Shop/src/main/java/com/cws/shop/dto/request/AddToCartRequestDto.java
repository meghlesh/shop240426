package com.cws.shop.dto.request;

import jakarta.validation.constraints.NotNull;

public class AddToCartRequestDto {
	@NotNull(message = "Product ID is required")
	private Long productId;

	public Long getProductId() {
		return productId;
	}

	public void setProductId(Long productId) {
		this.productId = productId;
	}
	
	
}
