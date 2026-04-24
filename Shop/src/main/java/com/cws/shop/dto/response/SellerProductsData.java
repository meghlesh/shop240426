package com.cws.shop.dto.response;

import java.math.BigDecimal;

public class SellerProductsData {
	
	private int totalSales;
	private int activeProducts;
	private BigDecimal revenue;
	private int pendingReviews;
	
	public SellerProductsData() {
	}

	public SellerProductsData(int totalSales, int activeProducts, BigDecimal revenue, int pendingReviews) {
		this.totalSales = totalSales;
		this.activeProducts = activeProducts;
		this.revenue = revenue;
		this.pendingReviews = pendingReviews;
	}

		public int getTotalSales() {
		return totalSales;
	}


	
	public void setTotalSales(int totalSales) {
		this.totalSales = totalSales;
	}


	
	public int getActiveProducts() {
		return activeProducts;
	}

	public void setActiveProducts(int activeProducts) {
		this.activeProducts = activeProducts;
	}
	
	public BigDecimal getRevenue() {
		return revenue;
	}

	public void setRevenue(BigDecimal revenue) {
		this.revenue = revenue;
	}

	public int getPendingReviews() {
		return pendingReviews;
	}
	
	public void setPendingReviews(int pendingReviews) {
		this.pendingReviews = pendingReviews;
	}	

}
