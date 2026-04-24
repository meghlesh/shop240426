package com.cws.shop.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.cws.shop.dto.response.NotificationResponseDto;
import com.cws.shop.model.User;
import com.cws.shop.repository.UserRepository;
import com.cws.shop.service.NotificationService;

@RestController
@RequestMapping("/api/notifications")
@PreAuthorize("hasAnyRole('ADMIN','SELLER')")
public class NotificationController {

    private final NotificationService notificationService;
    private final UserRepository userRepository;

    public NotificationController(NotificationService notificationService, UserRepository userRepository) {
        this.notificationService = notificationService;
        this.userRepository = userRepository;
    }

    private User getCurrentUser(Authentication authentication) {
        String email = authentication.getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    @GetMapping("/latest")
    public ResponseEntity<List<NotificationResponseDto>> getLatestNotifications(Authentication authentication) {

        User user = getCurrentUser(authentication);

        return ResponseEntity.ok(
                notificationService.getUserNotifications(user.getId())
        );
    }

    @GetMapping("/unread-count")
    public ResponseEntity<Long> getUnreadCount(Authentication authentication) {

        User user = getCurrentUser(authentication);

        return ResponseEntity.ok(
                notificationService.getUnreadCount(user.getId())
        );
    }

    @PutMapping("/{id}/read")
    public ResponseEntity<Void> markAsRead(@PathVariable Long id, Authentication authentication) {

        User user = getCurrentUser(authentication);

        notificationService.markAsRead(id, user.getId());

        return ResponseEntity.noContent().build();
    }
}