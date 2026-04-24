package com.cws.shop.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cws.shop.model.CartItem;

public interface CartItemRepository extends JpaRepository<CartItem, Long>{

}
