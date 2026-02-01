package com.ecommerce.catalog_service.messagequeue;

import com.ecommerce.catalog_service.dto.OrderMessage;
import com.ecommerce.catalog_service.entity.CatalogEntity;
import com.ecommerce.catalog_service.exception.InvalidStockQuantityException;
import com.ecommerce.catalog_service.repository.CatalogRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class KafkaConsumer {
    private final CatalogRepository catalogRepository;

    @KafkaListener(topics = "order-success-topic")
    @Transactional
    public void decreaseStock(String kafkaMessage) {
        log.info("Kafka Message : {}", kafkaMessage);

        ObjectMapper objectMapper = new ObjectMapper();

        try {
            OrderMessage message = objectMapper.readValue(kafkaMessage, OrderMessage.class);

            for (OrderMessage.OrderItem item : message.getOrderItems()) {
                String productId = item.getProductId();
                Integer stock = item.getStock();

                if (productId != null && stock != null) {
                    CatalogEntity entity = catalogRepository.findByProductId(productId);

                    if (entity != null) {
                        entity.decreaseStock(stock);
                        log.info("Stock decreased - Product: {}, Amount: {}, Remaining: {}",
                                productId, stock, entity.getStock());
                    }
                }
            }

        } catch (JsonProcessingException e) {
            log.error("Json Parsing Error : {}", e.getMessage());
        }
    }

    @KafkaListener(topics = "order-cancel-topic")
    @Transactional
    public void restoreStock(String kafkaMessage) {
        log.info("Kafka Message : {}", kafkaMessage);

        ObjectMapper objectMapper = new ObjectMapper();

        try {
            OrderMessage message = objectMapper.readValue(kafkaMessage, OrderMessage.class);

            for (OrderMessage.OrderItem item : message.getOrderItems()) {
                String productId = item.getProductId();
                Integer stock = item.getStock();

                if (productId != null && stock != null) {
                    CatalogEntity entity = catalogRepository.findByProductId(productId);

                    if (entity != null) {
                        entity.increaseStock(stock);
                        log.info("Stock increased - Product: {}, Amount: {}, Remaining: {}",
                                productId, stock, entity.getStock());
                    }
                }
            }
        } catch (JsonProcessingException e) {
            log.error("Json Parsing Error : {}", e.getMessage());
        }
    }
}
