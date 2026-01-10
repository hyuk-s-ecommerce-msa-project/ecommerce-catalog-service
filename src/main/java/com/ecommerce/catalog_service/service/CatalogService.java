package com.ecommerce.catalog_service.service;

import com.ecommerce.catalog_service.entity.CatalogEntity;

import java.util.List;

public interface CatalogService {
    Iterable<CatalogEntity> getAllCatalogs();
}
