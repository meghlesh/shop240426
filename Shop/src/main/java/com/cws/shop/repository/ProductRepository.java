package com.cws.shop.repository;


import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.cws.shop.model.Product;
import com.cws.shop.model.ProductStatus;

public interface ProductRepository extends JpaRepository<Product, Long>{

	List<Product> findByStatus(ProductStatus status);

	List<Product> findByCreatedByEmailAndStatus(String email, ProductStatus approved);
	
	@Query(value = """
			SELECT * FROM products p
			WHERE p.status = 'APPROVED'
			AND (
			    LOWER(p.name) LIKE LOWER(CONCAT('%', :q, '%'))
			 OR JSON_SEARCH(p.description, 'one', :q) IS NOT NULL
			 OR CAST(p.price AS char) LIKE CONCAT('%', :q, '%')
			)
			""", nativeQuery = true)
			List<Product> searchProducts(@Param("q") String keyword, Pageable pageable);

	List<Product> findByCreatedByIdAndNameContainingIgnoreCase(Long id, String keyword);

}
