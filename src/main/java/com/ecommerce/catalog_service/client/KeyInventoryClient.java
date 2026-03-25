package com.ecommerce.catalog_service.client;

import com.ecommerce.catalog_service.vo.ResponseKeys;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@FeignClient(
        name = "key-inventory-service",
        url = "http://key-inventory-service:8085"
)
public interface KeyInventoryClient {
    @GetMapping("/key-inventory/keys")
    List<ResponseKeys> getKeys();
}
