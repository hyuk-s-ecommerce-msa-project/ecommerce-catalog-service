package com.ecommerce.catalog_service.service;

import com.ecommerce.catalog_service.dto.CatalogDto;
import com.ecommerce.catalog_service.entity.CatalogEntity;

import java.util.List;

public interface CatalogService {
    Iterable<CatalogEntity> getAllCatalogs();
    CatalogDto getCatalogByProductId(String productId);
    CatalogDto increaseStock(String productId, Integer stock);
    CatalogDto decreaseStock(String productId, Integer stock);
}
