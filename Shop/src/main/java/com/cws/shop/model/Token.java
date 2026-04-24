package com.cws.shop.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name="tokens")
public class Token {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	@Column(nullable = false, unique = true)
	private String token;
	
	@Enumerated(EnumType.STRING)
	@Column(nullable = false, length = 20)
	private TokenType tokenType;
	
	@Column(nullable = false)
	private LocalDateTime expiryDate;
	
	@ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
	private User user;
	
	public Token(){}

	public Token(long id, String token, TokenType tokenType, LocalDateTime expiryDate, User user) {
		this.id = id;
		this.token = token;
		this.tokenType = tokenType;
		this.expiryDate = expiryDate;
		this.user = user;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public TokenType getTokenType() {
		return tokenType;
	}

	public void setTokenType(TokenType tokenType) {
		this.tokenType = tokenType;
	}

	public LocalDateTime getExpiryDate() {
		return expiryDate;
	}

	public void setExpiryDate(LocalDateTime expiryDate) {
		this.expiryDate = expiryDate;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@Override
	public String toString() {
		return "Token "
				+ "[id=" + id + 
				", token=" + token + 
				", tokenType=" + tokenType + 
				", expiryDate=" + expiryDate+ 
				", user=" + user + "]";
	}
	
	
}
