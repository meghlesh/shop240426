package com.cws.shop.repository;

import com.cws.shop.model.Token;
import com.cws.shop.model.TokenType;
import com.cws.shop.model.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface TokenRepository extends JpaRepository<Token, Long> {

    Optional<Token> findByTokenAndTokenType(String token, TokenType type);

    // delete all expired tokens
    void deleteByExpiryDateBefore(LocalDateTime now);
    
    List<Token> findByUserAndTokenType(User user, TokenType tokenType);
}