package com.cws.shop.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.cws.shop.model.Role;
import com.cws.shop.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
	Optional<User> findByEmail(String email);
	
	boolean existsByEmail(String email);

	List<User> findByRole(Role role);

	long countByActiveTrue();

	List<User> findByRoleIn(List<Role> of);
	
	@Query("""
		    SELECT u FROM User u
		    WHERE LOWER(u.name) LIKE LOWER(CONCAT('%', :q, '%'))
		       OR LOWER(u.email) LIKE LOWER(CONCAT('%', :q, '%'))
		       OR u.mobileNumber LIKE CONCAT('%', :q, '%')
		""")
		List<User> searchUsers(@Param("q") String keyword, Pageable pageable);
}
