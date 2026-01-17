package com.ecommerce.catalog_service.repository;

import com.ecommerce.catalog_service.entity.CatalogEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CatalogRepository extends CrudRepository<CatalogEntity, Long> {
    @EntityGraph(attributePaths = {"images"})
    CatalogEntity findByProductId(String productId);

    @Override
    @EntityGraph(attributePaths = {"images"})
    List<CatalogEntity> findAll();
}
