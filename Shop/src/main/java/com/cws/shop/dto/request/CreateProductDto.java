package com.cws.shop.dto.request;

import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public class CreateProductDto {
	@NotBlank(message = "Product name is required")
    @Size(max = 100, message = "Name cannot exceed 100 characters")
	private String name;
	
	@Size(max = 500, message = "Description cannot exceed 500 characters")
	private List<String> description;
	
	@NotNull(message = "Price is required")
    @Positive(message = "Price must be greater than 0")
	private Double price;
	
	@NotBlank(message = "Image URL is required")
	private String imageUrl;

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

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
	
	
}
