package com.cws.shop.serviceImpl;

import java.util.List;
import java.util.Optional;
import java.util.ArrayList;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.cws.shop.dto.response.CartItemDto;
import com.cws.shop.dto.response.CartResponseDto;
import com.cws.shop.exception.ProductNotFoundException;
import com.cws.shop.exception.UserNotFoundException;
import com.cws.shop.model.Cart;
import com.cws.shop.model.CartItem;
import com.cws.shop.model.Product;
import com.cws.shop.model.User;
import com.cws.shop.repository.CartRepository;
import com.cws.shop.repository.ProductRepository;
import com.cws.shop.repository.UserRepository;
import com.cws.shop.service.CartService;

@Service
public class CartServiceImpl implements CartService{
	
	private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    
    public CartServiceImpl(CartRepository cartRepository,ProductRepository productRepository,UserRepository userRepository) {
    	this.cartRepository = cartRepository;
    	this.productRepository = productRepository;
    	this.userRepository = userRepository;
    }
    
    private User getCurrentUser() {

        String email = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();

        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
    }
    
    private Cart getOrCreateCart(User user) {
        return cartRepository.findByUser(user)
                .orElseGet(() -> {
                    Cart cart = new Cart();
                    cart.setUser(user);
                    cart.setItems(new ArrayList<>()); // ✅ works
                    return cartRepository.save(cart);
                });
    }
    
	@Override
	public CartResponseDto addToCart(Long productId) {
		User user = getCurrentUser();

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Product not found"));

        Cart cart = getOrCreateCart(user);

        //checks duplicate
        Optional<CartItem> existingItem = cart.getItems().stream()
                .filter(i -> i.getProduct().getId().equals(productId))
                .findFirst();

        if (existingItem.isPresent()) {
            // already exists → return cart
            return mapToResponse(cart);
        }

        //quantity = 1
        CartItem item = new CartItem();
        item.setProduct(product);
        item.setDescription(product.getDescription());
        item.setQuantity(1);
        item.setPriceAtTime(product.getPrice());
        item.setCart(cart);

        cart.getItems().add(item);

        cartRepository.save(cart);

        return mapToResponse(cart);
    }

    // ✅ GET CART
    @Override
    public CartResponseDto getCart() {
    	
        User user = getCurrentUser();
        Cart cart = getOrCreateCart(user);

        return mapToResponse(cart);
	}

    @Override
    public void removeItem(Long productId) {

        User user = getCurrentUser();
        Cart cart = getOrCreateCart(user);

        cart.getItems().removeIf(i ->
                i.getProduct().getId().equals(productId)
        );

        cartRepository.save(cart);
    }
    
    // ✅ MAPPER
    private CartResponseDto mapToResponse(Cart cart) {

        CartResponseDto response = new CartResponseDto();
        response.setCartId(cart.getId());

        List<CartItemDto> items = cart.getItems().stream().map(i -> {
            CartItemDto dto = new CartItemDto();
            dto.setProductId(i.getProduct().getId());
            dto.setProductName(i.getProduct().getName());
            dto.setQuantity(i.getQuantity());
            dto.setPrice(i.getPriceAtTime());
            dto.setDescription(i.getProduct().getDescription());
            return dto;
        }).toList();

        double subtotal = items.stream()
                .mapToDouble(i -> i.getPrice() * i.getQuantity())
                .sum();

        double tax = subtotal * 0.18;   // GST 18%
        double discount = 0;
        double total = subtotal + tax - discount;
        
        subtotal = Math.round(subtotal * 100.0) / 100.0;
        tax = Math.round(tax * 100.0) / 100.0;
        total = Math.round(total * 100.0) / 100.0;
        
        response.setItems(items);
        response.setSubtotal(subtotal);
        response.setTax(tax);
        response.setDiscount(discount);
        response.setTotalAmount(total);

        return response;
    }
}
