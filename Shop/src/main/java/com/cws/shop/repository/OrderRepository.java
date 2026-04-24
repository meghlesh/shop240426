package com.cws.shop.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.cws.shop.model.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {

    // Total Revenue
    @Query("SELECT SUM(o.totalAmount) FROM Order o")
    Double getTotalRevenue();

    // Total Commission
    @Query("SELECT SUM(o.commissionAmount) FROM Order o")
    Double getTotalCommission();

    // Search Orders
    @Query("""
        SELECT o FROM Order o
        WHERE 
            CAST(o.id AS char) LIKE CONCAT('%', :q, '%')
         OR CAST(o.totalAmount AS char) LIKE CONCAT('%', :q, '%')
         OR CAST(o.commissionAmount AS char) LIKE CONCAT('%', :q, '%')
    """)
    List<Order> searchOrders(@Param("q") String keyword, Pageable pageable);

    // Orders by Seller (Product Owner)
    @Query("""
        SELECT o FROM Order o
        WHERE o.product.createdBy.id = :sellerId
    """)
    List<Order> findByProductSellerId(@Param("sellerId") Long sellerId);

}