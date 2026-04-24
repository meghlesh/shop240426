package com.cws.shop.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cws.shop.model.Cart;
import com.cws.shop.model.User;

public interface CartRepository extends JpaRepository<Cart, Long>{

	Optional<Cart> findByUser(User user);

}
