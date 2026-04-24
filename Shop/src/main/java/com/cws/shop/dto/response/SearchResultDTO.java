package com.cws.shop.dto.response;

public class SearchResultDTO {

    private String type;   // PRODUCT / USER / ORDER
    private String title;  // display name
    private Long id;

    public SearchResultDTO(String type, String title, Long id) {
        this.type = type;
        this.title = title;
        this.id = id;
    }

    public String getType() { 
    	return type; 
    }
    public String getTitle() { 
    	return title; 
    }
    public Long getId() { 
    	return id; 
    }
}