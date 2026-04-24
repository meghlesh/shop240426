package com.cws.shop.serviceImpl;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.cws.shop.model.Payment;
import com.cws.shop.repository.PaymentRepository;
import com.cws.shop.service.PaymentService;
import com.cws.shop.util.RazorpaySignatureUtil;
import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.Utils;

import java.util.HashMap;
import java.util.Map;

@Service
public class PaymentServiceImpl implements PaymentService {

    @Autowired
    private RazorpayClient razorpayClient;

    @Autowired
    private PaymentRepository paymentRepository;

    @Value("${razorpay.key-secret}")
    private String razorpaySecret;

    // =========================
    // CREATE ORDER
    // =========================
    @Override
    public JSONObject createOrder(Double amount, Long userId) throws Exception {

        JSONObject options = new JSONObject();
        options.put("amount", amount * 100); // convert to paise
        options.put("currency", "INR");
        options.put("receipt", "order_rcptid_" + System.currentTimeMillis());
        options.put("payment_capture", 1);

        Order razorpayOrder = razorpayClient.orders.create(options);

        // Save payment in DB
        Payment payment = new Payment();
        payment.setOrderId(razorpayOrder.get("id"));
        payment.setBuyerId(userId);
        payment.setAmount(amount);
        payment.setStatus("CREATED");

        paymentRepository.save(payment);

        return new JSONObject(razorpayOrder.toString());
    }

    // =========================
    // VERIFY PAYMENT
    // =========================
    @Override
    public boolean verifyPayment(String razorpayOrderId,
                                 String razorpayPaymentId,
                                 String razorpaySignature) throws Exception {

        Payment payment = paymentRepository.findByOrderId(razorpayOrderId);

        if (payment == null) {
            return false;
        }

        JSONObject params = new JSONObject();
        params.put("razorpay_order_id", razorpayOrderId);
        params.put("razorpay_payment_id", razorpayPaymentId);
        params.put("razorpay_signature", razorpaySignature);

        boolean isValid = Utils.verifyPaymentSignature(params, razorpaySecret);

        if (isValid) {
            payment.setStatus("PAID");
            payment.setPaymentId(razorpayPaymentId);
            payment.setSignature(razorpaySignature);
        } else {
            payment.setStatus("FAILED");
        }

        paymentRepository.save(payment);

        return isValid;
    }

    @Override
    public boolean handleWebhook(String payload, String signature) throws Exception {

    	boolean isValid = Utils.verifyWebhookSignature(
    	        payload,
    	        signature,
    	        razorpaySecret
    	);

        if (isValid) {

            JSONObject json = new JSONObject(payload);

            String orderId = json
                    .getJSONObject("payload")
                    .getJSONObject("payment")
                    .getJSONObject("entity")
                    .getString("order_id");

            String paymentStatus = json
                    .getJSONObject("payload")
                    .getJSONObject("payment")
                    .getJSONObject("entity")
                    .getString("status");

            Payment payment = paymentRepository.findByOrderId(orderId);

            if (payment != null) {

                if ("captured".equals(paymentStatus)) {
                    payment.setStatus("PAID");
                } else {
                    payment.setStatus("FAILED");
                }

                paymentRepository.save(payment);
            }
        }

        return isValid;
    }
}