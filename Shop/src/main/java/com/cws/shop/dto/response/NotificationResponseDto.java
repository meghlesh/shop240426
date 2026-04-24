package com.cws.shop.dto.response;

import java.time.LocalDateTime;

public class NotificationResponseDto {
	private Long id;
	private String title;
    private String message;
    private LocalDateTime createdAt;
    private boolean isRead;
	
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    
	public String getTitle() {
		return title;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	public String getMessage() {
		return message;
	}
	
	public void setMessage(String message) {
		this.message = message;
	}
	
	public LocalDateTime getCreatedAt() {
		return createdAt;
	}
	
	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}
	
	public boolean isRead() {
		return isRead;
	}
	
	public void setRead(boolean isRead) {
		this.isRead = isRead;
	}
}

