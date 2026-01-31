package com.ecommerce.catalog_service.controller;

import com.ecommerce.catalog_service.dto.CatalogDto;
import com.ecommerce.catalog_service.dto.CatalogStockDto;
import com.ecommerce.catalog_service.dto.SteamAppListResponse;
import com.ecommerce.catalog_service.entity.CatalogEntity;
import com.ecommerce.catalog_service.service.CatalogService;
import com.ecommerce.catalog_service.service.SteamApiService;
import com.ecommerce.catalog_service.vo.ResponseCatalog;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/catalog-service")
public class CatalogController {
    private final CatalogService catalogService;
    private final SteamApiService steamApiService;
    private final Environment env;
    private final ModelMapper modelMapper;

    @GetMapping("/health-check")
    public String status() {
        return String.format("It's working in Catalog Service on LOCAL Port %s (Server Port %s)",
                env.getProperty("local.server.port"), env.getProperty("server.port"));
    }

    @GetMapping("/catalogs")
    public ResponseEntity<List<ResponseCatalog>> getCatalogs() {
        Iterable<CatalogEntity> catalogList = catalogService.getAllCatalogs();

        List<ResponseCatalog> result = new ArrayList<>();

        catalogList.forEach(catalog -> {
            CatalogDto dto = catalogService.getCatalogByProductId(catalog.getProductId());
            result.add(modelMapper.map(dto, ResponseCatalog.class));
        });

        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @PostMapping("/catalogs/stock/increase")
    public ResponseEntity<List<ResponseCatalog>> increaseStock(@RequestBody List<String> productIds) {
        List<CatalogDto> catalogDto = catalogService.increaseStock(productIds);

        List<ResponseCatalog> response = catalogDto.stream().map(dto -> modelMapper.map(dto, ResponseCatalog.class)).toList();

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("/catalogs/{productId}/stock/decrease")
    public ResponseEntity<List<ResponseCatalog>> decreaseStock(@RequestBody List<String> productIds) {
        List<CatalogDto> catalogDto = catalogService.decreaseStock(productIds);

        List<ResponseCatalog> response = catalogDto.stream().map(dto -> modelMapper.map(dto, ResponseCatalog.class)).toList();

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/catalogs/{productId}")
    public ResponseEntity<ResponseCatalog> getCatalog(@PathVariable("productId") String productId) {
        CatalogDto catalogDto = catalogService.getCatalogByProductId(productId);
        ResponseCatalog response = modelMapper.map(catalogDto, ResponseCatalog.class);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/steam/data")
    public ResponseEntity<List<SteamAppListResponse.SteamAppDto>> getSteamAppList() {
        List<SteamAppListResponse.SteamAppDto> data = steamApiService.fetchSteamApps();

        return ResponseEntity.status(HttpStatus.OK).body(data);
    }

    @PostMapping("/catalogs/queries")
    public ResponseEntity<List<ResponseCatalog>> getCatalogsByProductIds(@RequestBody List<String> productIds) {
        List<CatalogDto> catalogDtoList = catalogService.getCatalogByProductIds(productIds);

        List<ResponseCatalog> response = catalogDtoList.stream().map(v -> modelMapper.map(v, ResponseCatalog.class)).toList();

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
