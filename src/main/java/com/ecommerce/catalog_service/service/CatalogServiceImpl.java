package com.ecommerce.catalog_service.service;

import com.ecommerce.catalog_service.entity.CatalogEntity;
import com.ecommerce.catalog_service.repository.CatalogRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Getter
@Slf4j
public class CatalogServiceImpl implements CatalogService {
    private final CatalogRepository catalogRepository;
    private final Environment env;

    @Override
    public Iterable<CatalogEntity> getAllCatalogs() {
        return catalogRepository.findAll();
    }
}
