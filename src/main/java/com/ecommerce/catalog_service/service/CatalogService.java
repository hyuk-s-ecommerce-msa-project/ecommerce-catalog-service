package com.ecommerce.catalog_service.service;

import com.ecommerce.catalog_service.dto.CatalogDto;
import com.ecommerce.catalog_service.entity.CatalogEntity;

import java.util.List;

public interface CatalogService {
    Iterable<CatalogEntity> getAllCatalogs();
    CatalogDto getCatalogByProductId(String productId);
    List<CatalogDto> increaseStock(List<String> productIds);
    List<CatalogDto> decreaseStock(List<String> productIds);
    List<CatalogDto> getCatalogByProductIds(List<String> productIds);
}
