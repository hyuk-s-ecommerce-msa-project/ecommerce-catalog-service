package com.ecommerce.catalog_service.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "catalog_category")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CatalogCategoryEntity {
    @Id
    private Long id;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String categoryName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "catalog_id")
    private CatalogEntity catalog;

    public static CatalogCategoryEntity create(Long id, String categoryName, CatalogEntity catalog) {
        CatalogCategoryEntity category = new CatalogCategoryEntity();

        category.id = id;
        category.categoryName = categoryName;
        category.catalog = catalog;
        return category;
    }
}
