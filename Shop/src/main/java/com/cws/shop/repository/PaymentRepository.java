package com.cws.shop.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cws.shop.model.Payment;

public interface PaymentRepository extends JpaRepository<Payment, Long>{
	Payment findByOrderId(String orderId);
}
