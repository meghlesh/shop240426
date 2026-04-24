package com.cws.shop.service;

import com.cws.shop.dto.response.CartResponseDto;

public interface CartService {
	CartResponseDto addToCart(Long productId);
	
	CartResponseDto getCart();
	
	void removeItem(Long productId);
}
