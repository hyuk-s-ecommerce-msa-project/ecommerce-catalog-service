package com.ecommerce.catalog_service.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class OrderMessage {
    private List<OrderItem> orderItems;

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class OrderItem {
        private String productId;
        private Integer stock;
    }
}
