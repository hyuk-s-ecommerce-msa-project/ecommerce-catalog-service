package com.ecommerce.catalog_service.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "catalog_image")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CatalogImageEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 500)
    private String imageUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "catalog_id")
    private CatalogEntity catalog;

    public static CatalogImageEntity create(String imageUrl, CatalogEntity catalog) {
        CatalogImageEntity image = new CatalogImageEntity();

        image.imageUrl = imageUrl;
        image.catalog = catalog;

        return image;
    }
}
