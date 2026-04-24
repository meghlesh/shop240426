package com.cws.shop.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cws.shop.model.Notification;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
	List<Notification> findTop4ByUserIdOrderByCreatedAtDesc(Long userId);

    List<Notification> findByUserIdOrderByCreatedAtDesc(Long userId);

    long countByUserIdAndIsReadFalse(Long userId);
    
    List<Notification> findByUserIdAndIsReadFalseOrderByCreatedAtDesc(Long userId);
}
