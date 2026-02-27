package com.ecommerce.catalog_service.entity;

import com.ecommerce.catalog_service.exception.InvalidStockQuantityException;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Table(name = "catalog")
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CatalogEntity {
    @Id
    private Long id;

    @Column(nullable = false, length = 120, unique = true)
    private String productId;
    @Column(nullable = false)
    private String productName;
    @Column(nullable = false)
    private Integer price;
    @Column(nullable = false)
    private Integer stock;
    @Column(nullable = false)
    private String headerImage;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String detailDescription;
    private String releaseDate;

    @OneToMany(mappedBy = "catalog", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CatalogImageEntity> images = new ArrayList<>();

    @OneToMany(mappedBy = "catalog", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CatalogGenreEntity> genres = new ArrayList<>();

    @OneToMany(mappedBy = "catalog", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CatalogCategoryEntity> categories = new ArrayList<>();

    @Column(nullable = false, updatable = false)
    @CreatedDate
    private LocalDateTime createdAt;

    public void decreaseStock(Integer qty) {
        if (qty < 1) {
            throw new InvalidStockQuantityException("Quantity to decrease must be at least 1.");
        }

        if (this.stock < qty) {
            throw new InvalidStockQuantityException("Out of stock");
        }
        this.stock -= qty;
    }

    public void increaseStock(Integer qty) {
        this.stock += qty;
    }

    public static CatalogEntity createCatalog(Long id, String productId, String productName, Integer price,
                                              Integer stock, String headerImage, String detailDescription, String releaseDate) {
        CatalogEntity catalog = new CatalogEntity();

        catalog.id = id;
        catalog.productId = productId;
        catalog.productName = productName;
        catalog.price = price;
        catalog.stock = stock;
        catalog.headerImage = headerImage;
        catalog.detailDescription = detailDescription;
        catalog.releaseDate = releaseDate;
        return catalog;
    }

    public void updateInfo(String productName, Integer price, String detailDescription, String releaseDate) {
        this.productName = productName;
        this.price = price;
        this.detailDescription = detailDescription;
        this.releaseDate = releaseDate;
    }

    public void addImage(Long id, String imageUrl) {
        this.images.add(CatalogImageEntity.create(id, imageUrl, this));
    }

    public void addGenre(Long id, String genreName) {
        this.genres.add(CatalogGenreEntity.create(id, genreName, this));
    }

    public void addCategory(Long id, String categoryName) {
        this.categories.add(CatalogCategoryEntity.create(id, categoryName, this));
    }

    public void clearCollections() {
        this.images.clear();
        this.genres.clear();
        this.categories.clear();
    }
}
