package com.ecommerce.catalog_service.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "catalog_genre")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CatalogGenreEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String genreName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "catalog_id")
    private CatalogEntity catalog;

    public static CatalogGenreEntity create(String genreName, CatalogEntity catalog) {
        CatalogGenreEntity genre = new CatalogGenreEntity();
        genre.genreName = genreName;
        genre.catalog = catalog;
        return genre;
    }
}
