package com.cws.shop.serviceImpl;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.cws.shop.dto.response.AdminDashboardDto;
import com.cws.shop.repository.UserRepository;
import com.cws.shop.repository.OrderRepository;
import com.cws.shop.service.AdminDashboardService;

@Service
public class AdminDashboardServiceImpl implements AdminDashboardService {

    private final UserRepository userRepository;
    private final OrderRepository orderRepository;

    public AdminDashboardServiceImpl(UserRepository userRepository,
                                     OrderRepository orderRepository) {
        this.userRepository = userRepository;
        this.orderRepository = orderRepository;
    }

    @Override
    public AdminDashboardDto getDashboardData() {

        AdminDashboardDto dto = new AdminDashboardDto();

        Double revenue = orderRepository.getTotalRevenue();
        Double commission = orderRepository.getTotalCommission();

        dto.setTotalRevenue(Optional.ofNullable(revenue).orElse(0.0));
        dto.setTotalCommission(Optional.ofNullable(commission).orElse(0.0));

        dto.setTotalUsers(Math.toIntExact(userRepository.count()));
        dto.setActiveUsers(Math.toIntExact(userRepository.countByActiveTrue()));

        return dto;
    }
}