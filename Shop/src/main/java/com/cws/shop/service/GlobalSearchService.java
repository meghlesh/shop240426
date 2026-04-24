package com.cws.shop.service;

import java.util.List;
import com.cws.shop.dto.response.SearchResultDTO;

public interface GlobalSearchService {

    List<SearchResultDTO> search(String keyword, String email);

}