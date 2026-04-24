package com.cws.shop.dto.response;

import java.util.List;

public class CartResponseDto {
	private Long cartId;
	private List<CartItemDto> items;
	private double subtotal;
	private double tax;
	private double discount;
	private double totalAmount;
	
	
	public Long getCartId() {
		return cartId;
	}
	public void setCartId(Long cartId) {
		this.cartId = cartId;
	}
	public List<CartItemDto> getItems() {
		return items;
	}
	public void setItems(List<CartItemDto> items) {
		this.items = items;
	}
	public double getSubtotal() {
		return subtotal;
	}
	public void setSubtotal(double subtotal) {
		this.subtotal = subtotal;
	}
	public double getTax() {
		return tax;
	}
	public void setTax(double tax) {
		this.tax = tax;
	}
	public double getDiscount() {
		return discount;
	}
	public void setDiscount(double discount) {
		this.discount = discount;
	}
	public double getTotalAmount() {
		return totalAmount;
	}
	public void setTotalAmount(double totalAmount) {
		this.totalAmount = totalAmount;
	}
}
