package com.ecommerce.catalog_service.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseCatalog {
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

    private LocalDateTime createdAt;
}
