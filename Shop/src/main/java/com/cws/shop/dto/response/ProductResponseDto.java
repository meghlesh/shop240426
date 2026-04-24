package com.cws.shop.dto.response;

import java.util.List;

public class ProductResponseDto {
	private Long id;
	private String name;
	private List<String> description;
	private double price;
	private String imageUrl;
	
	public ProductResponseDto(Long id, String name, List<String> list, double price, String imageUrl) {
		this.id = id;
		this.name = name;
		this.description = list;
		this.price = price;
		this.imageUrl = imageUrl;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<String> getDescription() {
		return description;
	}

	public void setDescription(List<String> description) {
		this.description = description;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
	
	
}
