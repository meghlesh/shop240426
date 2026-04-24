package com.cws.shop.controller;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cws.shop.dto.request.CreateOrderRequest;
import com.cws.shop.dto.request.PaymentVerifyRequest;
import com.cws.shop.service.PaymentService;

@RestController
@RequestMapping("/api/payment")
public class PaymentController {
	
	@Autowired
	private PaymentService paymentService;
	
	@PostMapping("/create-order")
	public String createOrder(@RequestBody CreateOrderRequest request) throws Exception{
		JSONObject order = paymentService.createOrder(request.getAmount(), request.getUserId());
		return order.toString();
	}
	
	@PostMapping("/verify-payment")
	public String verifyPayment(@RequestBody PaymentVerifyRequest request) throws Exception{
		boolean result = paymentService.verifyPayment(request.getRazorpay_order_id(), request.getRazorpay_payment_id(), request.getRazorpay_signature());
		if(result){
            return "Payment Verified";
        }

        return "Invalid Payment";
	}
}
