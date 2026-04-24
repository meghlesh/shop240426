package com.cws.shop.serviceImpl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.cws.shop.dto.response.SearchResultDTO;
import com.cws.shop.model.Product;
import com.cws.shop.model.User;
import com.cws.shop.model.Order;
import com.cws.shop.repository.ProductRepository;
import com.cws.shop.repository.UserRepository;
import com.cws.shop.repository.OrderRepository;
import com.cws.shop.service.GlobalSearchService;

@Service
public class GlobalSearchServiceImpl implements GlobalSearchService {

    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;

    public GlobalSearchServiceImpl(ProductRepository productRepository,
                                   UserRepository userRepository,
                                   OrderRepository orderRepository) {
        this.productRepository = productRepository;
        this.userRepository = userRepository;
        this.orderRepository = orderRepository;
    }

    @Override
    public List<SearchResultDTO> search(String keyword, String email) {

        List<SearchResultDTO> results = new ArrayList<>();

        if (keyword == null || keyword.trim().isEmpty()) {
            return results;
        }

        keyword = keyword.trim();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        PageRequest limit = PageRequest.of(0, 5);

        // ================= ADMIN =================
        if (user.getRole().name().equals("ADMIN")) {

            // PRODUCTS (ALL)
            List<Product> products = productRepository.searchProducts(keyword, limit);
            for (Product p : products) {
                results.add(new SearchResultDTO("PRODUCT", p.getName(), p.getId()));
            }

            // USERS (ALL)
            List<User> users = userRepository.searchUsers(keyword, limit);
            for (User u : users) {
                results.add(new SearchResultDTO("USER", u.getName(), u.getId()));
            }

            // ORDERS (ALL)
            List<Order> orders = orderRepository.searchOrders(keyword, limit);
            for (Order o : orders) {
                results.add(new SearchResultDTO(
                    "ORDER",
                    "Order #" + o.getId(),
                    o.getId()
                ));
            }
        }

        // ================= SELLER =================
        else if (user.getRole().name().equals("SELLER")) {

            // 🔥 ONLY SELLER PRODUCTS
            List<Product> products =
                    productRepository.findByCreatedByIdAndNameContainingIgnoreCase(
                            user.getId(), keyword);

            for (Product p : products) {
                results.add(new SearchResultDTO("PRODUCT", p.getName(), p.getId()));
            }

            // 🔥 OPTIONAL: SELLER ORDERS (ONLY HIS PRODUCTS)
            List<Order> orders =
                    orderRepository.findByProductSellerId(user.getId());

            for (Order o : orders) {
                results.add(new SearchResultDTO(
                    "ORDER",
                    "Order #" + o.getId(),
                    o.getId()
                ));
            }
        }

        // ================= USER =================
        else {

            // Only public approved products
            List<Product> products = productRepository.searchProducts(keyword, limit);

            for (Product p : products) {
                results.add(new SearchResultDTO("PRODUCT", p.getName(), p.getId()));
            }
        }

        return results;
    }
}