package com.ecommerce.catalog_service.service;

import com.ecommerce.catalog_service.dto.CatalogDto;
import com.ecommerce.catalog_service.entity.CatalogCategoryEntity;
import com.ecommerce.catalog_service.entity.CatalogEntity;
import com.ecommerce.catalog_service.entity.CatalogGenreEntity;
import com.ecommerce.catalog_service.entity.CatalogImageEntity;
import com.ecommerce.catalog_service.exception.InvalidStockQuantityException;
import com.ecommerce.catalog_service.repository.CatalogRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Getter
@Slf4j
public class CatalogServiceImpl implements CatalogService {
    private final CatalogRepository catalogRepository;
    private final ModelMapper modelMapper;
    private final Environment env;

    @Override
    public CatalogDto getCatalogByProductId(String productId) {
        CatalogEntity catalogEntity = catalogRepository.findByProductId(productId);

        if (catalogEntity == null) {
            throw new EntityNotFoundException("Product not found");
        }

        return convertEntityToDto(catalogEntity);
    }

    @Override
    @Transactional
    public CatalogDto increaseStock(String productId, Integer stock) {
        CatalogEntity catalogEntity = catalogRepository.findByProductId(productId);

        if (catalogEntity == null) {
            throw new EntityNotFoundException("Product not found");
        }

        catalogEntity.increaseStock(stock);

        return convertEntityToDto(catalogEntity);
    }

    @Override
    @Transactional
    public CatalogDto decreaseStock(String productId, Integer stock) {
        CatalogEntity catalogEntity = catalogRepository.findByProductId(productId);

        if (catalogEntity == null) {
            throw new EntityNotFoundException("Product not found");
        }

        if (stock < 1) {
            throw new InvalidStockQuantityException("stock must be at least 1");
        }

        catalogEntity.decreaseStock(stock);

        return convertEntityToDto(catalogEntity);
    }

    @Override
    public List<CatalogEntity> getAllCatalogs() {
        return catalogRepository.findAll();
    }

    private CatalogDto convertEntityToDto(CatalogEntity entity) {
        CatalogDto dto = modelMapper.map(entity, CatalogDto.class);

        dto.setImages(entity.getImages().stream()
                .map(CatalogImageEntity::getImageUrl).toList());
        dto.setGenres(entity.getGenres().stream()
                .map(CatalogGenreEntity::getGenreName).toList());
        dto.setCategories(entity.getCategories().stream()
                .map(CatalogCategoryEntity::getCategoryName).toList());

        return dto;
    }
}
