package com.ecommerce.catalog_service.service.connector;

import com.ecommerce.catalog_service.client.KeyInventoryClient;
import com.ecommerce.catalog_service.vo.ResponseKeys;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class InternalServiceConnector {
    private final KeyInventoryClient keyInventoryClient;

    @CircuitBreaker(name = "CatalogService_getKey_circuitBreaker", fallbackMethod = "fallbackGetKeys")
    public List<ResponseKeys> getKeys() {
        return keyInventoryClient.getKeys();
    }

    private List<ResponseKeys> fallbackGetKeys(Throwable throwable) {
        log.error("Can't get Keys, reason : {}", throwable.getMessage());

        throw new RuntimeException("게임 키를 가져올 수 없습니다.");
    }
}
