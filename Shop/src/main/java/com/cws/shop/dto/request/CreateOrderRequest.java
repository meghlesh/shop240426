package com.cws.shop.dto.request;

import lombok.Data;

@Data
public class CreateOrderRequest {
	private double amount;
	private Long userId;

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}
	
	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}
}
