package com.cws.shop.service;

import java.util.List;

import com.cws.shop.dto.response.NotificationResponseDto;
import com.cws.shop.model.NotificationType;
import com.cws.shop.model.User;

public interface NotificationService {
	
	void sendNotification(User user, String title, String message, NotificationType type);

    // Get latest notificatios for user dashboard
    List<NotificationResponseDto> getUserNotifications(Long userId);

    // Get all notifications (for "View All" page)
    List<NotificationResponseDto> getAllUserNotifications(Long userId);

    // Get unread notification count (for bell icon)
    long getUnreadCount(Long userId);

    // Mark notification as read (optional but important)
    void markAsRead(Long notificationId, Long userId);
    
    void notifyAdmins(String title, String message, NotificationType type);
    
    List<NotificationResponseDto> getUnreadNotifications(Long userId);

}
