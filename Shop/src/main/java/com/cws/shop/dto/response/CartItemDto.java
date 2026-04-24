package com.cws.shop.dto.response;

import java.util.List;

public class CartItemDto {
	private Long productId;
	
	private String productName;
	
	private int quantity;
	
	private double price;
	
	private List<String> description;

	public Long getProductId() {
		return productId;
	}

	public void setProductId(Long productId) {
		this.productId = productId;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}
	
	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public List<String> getDescription() {
		return description;
	}

	public void setDescription(List<String> list) {
		this.description = list;
	}
}
