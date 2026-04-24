package com.cws.shop.serviceImpl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.cws.shop.dto.request.CreateProductDto;
import com.cws.shop.dto.response.ProductResponseDto;
import com.cws.shop.exception.ProductNotFoundException;
import com.cws.shop.exception.UnauthorizedActionException;
import com.cws.shop.exception.UserNotFoundException;
import com.cws.shop.model.NotificationType;
import com.cws.shop.model.Product;
import com.cws.shop.model.ProductStatus;
import com.cws.shop.model.Role;
import com.cws.shop.model.User;
import com.cws.shop.repository.ProductRepository;
import com.cws.shop.repository.UserRepository;
import com.cws.shop.service.NotificationService;
import com.cws.shop.service.ProductService;

@Service
public class ProductServiceImpl implements ProductService {
	
	private final ProductRepository productRepository;
	private final UserRepository userRepository;
	private final NotificationService notificationService; // new variable added 
	
	
	public ProductServiceImpl(ProductRepository productRepository, UserRepository userRepository,
			NotificationService notificationservice) {
		this.productRepository = productRepository;
		this.userRepository = userRepository;
		this.notificationService = notificationservice;
	}

	@Override
	public ProductResponseDto createProduct(CreateProductDto dto) {
		Product product = new Product();
        product.setName(dto.getName());
        product.setDescription(dto.getDescription());
        product.setPrice(dto.getPrice());
        product.setImageUrl(dto.getImageUrl());
        product.setStatus(ProductStatus.PENDING);
        
        User user = getCurrentUser();
        product.setCreatedBy(user);
        
        Product saved = productRepository.save(product);
        
        // added for the notifications when seller added the product.
        notificationService.notifyAdmins(
        	    "New Product Submitted",
        	    "Product '" + saved.getName() + "' submitted by " + user.getName() + " needs admin approval",
        	    NotificationType.PRODUCT
        	);

        return mapToDto(saved);
	}

	private User getCurrentUser() {
	    String email = SecurityContextHolder
	            .getContext()
	            .getAuthentication()
	            .getName();

	    return userRepository.findByEmail(email)
	            .orElseThrow(() -> new UserNotFoundException("User not found"));
	}
	
	@Override
	public List<ProductResponseDto> getAllProducts() {
		return productRepository.findByStatus(ProductStatus.APPROVED)
	            .stream()
	            .map(this::mapToDto)
	            .collect(Collectors.toList());
	}

	@Override
	public ProductResponseDto getProductById(Long id) {
		Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product not found"));

        return mapToDto(product);
	}

	@Override
	public ProductResponseDto updateProduct(Long id, CreateProductDto dto) {

	    Product product = productRepository.findById(id)
	            .orElseThrow(() -> new ProductNotFoundException("Product not found"));

	    User currentUser = getCurrentUser();

	    // Seller CHECK
	    if (!product.getCreatedBy().getId().equals(currentUser.getId())) {
	        throw new UnauthorizedActionException("You are not allowed to update this product");
	    }

	    product.setName(dto.getName());
	    product.setDescription(dto.getDescription());
	    product.setPrice(dto.getPrice());
	    product.setImageUrl(dto.getImageUrl());

	    return mapToDto(productRepository.save(product));
	}

	@Override
	public void deleteProduct(Long id) {

	    Product product = productRepository.findById(id)
	            .orElseThrow(() -> new ProductNotFoundException("Product not found"));

	    User currentUser = getCurrentUser();

	    // Seller CHECK
	    if (!product.getCreatedBy().getId().equals(currentUser.getId())) {
	        throw new UnauthorizedActionException("You are not allowed to delete this product");
	    }

	    productRepository.delete(product);
	}
	
	private ProductResponseDto mapToDto(Product product) {
        return new ProductResponseDto(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getImageUrl()
        );
    }
	
	@Override
    public String approveProduct(Long id) {

        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product not found"));

        if (product.getStatus() == ProductStatus.APPROVED) {
            return "Product already approved";
        }

        if (product.getStatus() == ProductStatus.REJECTED) {
            return "Rejected product cannot be approved";
        }

        product.setStatus(ProductStatus.APPROVED);
        productRepository.save(product);

        notificationService.sendNotification(
                product.getCreatedBy(),
                "Product Approved",
                product.getName() + " has been approved",
                NotificationType.PRODUCT
        );

        return "Product approved";
    }

    @Override
    public String rejectProduct(Long id) {

        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product not found"));

        if (product.getStatus() == ProductStatus.REJECTED) {
            return "Product already rejected";
        }

        if (product.getStatus() == ProductStatus.APPROVED) {
            return "Approved product cannot be rejected";
        }

        product.setStatus(ProductStatus.REJECTED);
        productRepository.save(product);

        notificationService.sendNotification(
                product.getCreatedBy(),
                "Product Rejected",
                product.getName() + " has been rejected",
                NotificationType.PRODUCT
        );

        return "Product rejected";
    }


	@Override
	public List<ProductResponseDto> getProductsByStatus(ProductStatus status) {
		return productRepository.findByStatus(status)
	            .stream()
	            .map(this::mapToDto)
	            .collect(Collectors.toList());
	}
	
	
	
	// added helper method for the notify the admins when there is new product added
//	private void notifyAdmins(Product product) {
//
//	    List<User> admins = userRepository.findByRole(Role.ADMIN);
//
//	    for (User admin : admins) {
//	        notificationService.sendNotification(
//	                admin,
//	                "New Product Added",
//	                product.getName() + " needs approval",
//	                NotificationType.PRODUCT
//	    
//	        );
//	    }
//	}
	
}
