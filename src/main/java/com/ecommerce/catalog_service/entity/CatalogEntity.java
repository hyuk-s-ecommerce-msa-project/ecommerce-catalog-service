package com.ecommerce.catalog_service.entity;

import com.ecommerce.catalog_service.exception.InvalidStockQuantityException;
import jakarta.persistence.*;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Getter
@Table(name = "catalog")
@EntityListeners(AuditingEntityListener.class)
public class CatalogEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 120, unique = true)
    private String productId;
    @Column(nullable = false)
    private String productName;
    @Column(nullable = false)
    private Integer price;
    @Column(nullable = false)
    private Integer stock;
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
}
