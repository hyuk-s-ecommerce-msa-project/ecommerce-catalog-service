package com.ecommerce.catalog_service.dto;

import lombok.Data;

import java.util.List;

@Data
public class CatalogDto {
    private String productId;
    private String productName;
    private Integer price;
    private Integer stock;
    private String headerImage;
    private String detailDescription;
    private String releaseDate;

    private List<String> images;
    private List<String> genres;
    private List<String> categories;

    private String orderId;
    private String userId;
}
