package com.cws.shop.dto.response;

public class AdminDashboardDto {
	private double totalRevenue;
    private double totalCommission;
    private int totalUsers;
    private int activeUsers;
	
    public double getTotalRevenue() {
		return totalRevenue;
	}
	public void setTotalRevenue(double totalRevenue) {
		this.totalRevenue = totalRevenue;
	}
	public double getTotalCommission() {
		return totalCommission;
	}
	public void setTotalCommission(double totalCommission) {
		this.totalCommission = totalCommission;
	}
	public int getTotalUsers() {
		return totalUsers;
	}
	public void setTotalUsers(int totalUsers) {
		this.totalUsers = totalUsers;
	}
	public int getActiveUsers() {
		return activeUsers;
	}
	public void setActiveUsers(int activeUsers) {
		this.activeUsers = activeUsers;
	}
    
}

