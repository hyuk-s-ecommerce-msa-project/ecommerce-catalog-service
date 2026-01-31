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
    public List<CatalogDto> getCatalogByProductIds(List<String> productIds) {
        List<CatalogEntity> catalogEntities = catalogRepository.findByProductIdIn(productIds);

        return catalogEntities.stream().map(this::convertEntityToDto).toList();
    }

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
    public List<CatalogDto> increaseStock(List<String> productIds) {
        List<CatalogEntity> catalogEntity = catalogRepository.findByProductIdIn(productIds);

        if (catalogEntity == null) {
            throw new EntityNotFoundException("Product not found");
        }

        catalogEntity.forEach(entities -> entities.increaseStock(1));

        return catalogEntity.stream().map(this::convertEntityToDto).toList();
    }

    @Override
    @Transactional
    public List<CatalogDto> decreaseStock(List<String> productIds) {
        List<CatalogEntity> catalogEntity = catalogRepository.findByProductIdIn(productIds);

        if (catalogEntity == null) {
            throw new EntityNotFoundException("Product not found");
        }

        catalogEntity.forEach(entities -> entities.decreaseStock(1));

        return catalogEntity.stream().map(this::convertEntityToDto).toList();
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
