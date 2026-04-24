package com.cws.shop.controller;

import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.cws.shop.dto.response.SearchResultDTO;
import com.cws.shop.service.GlobalSearchService;

@RestController
@RequestMapping("/api")
public class GlobalSearchController {

    private final GlobalSearchService searchService;

    public GlobalSearchController(GlobalSearchService searchService) {
        this.searchService = searchService;
    }

//    @GetMapping("/search")
//    public List<SearchResultDTO> search(@RequestParam("q") String keyword) {
//        return searchService.search(keyword);
//    }
    
    @GetMapping("/search")
    public List<SearchResultDTO> search(
            @RequestParam String q,
            Authentication authentication) {

        String email = authentication.getName();

        return searchService.search(q, email);
    }
}
