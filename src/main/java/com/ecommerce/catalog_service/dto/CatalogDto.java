package com.ecommerce.catalog_service.dto;

import lombok.Data;

@Data
public class CatalogDto {
    private String productId;
    private Integer price;

    private String orderId;
    private String userId;
}
