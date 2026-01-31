package com.ecommerce.catalog_service.service;

import com.ecommerce.catalog_service.client.KeyInventoryClient;
import com.ecommerce.catalog_service.dto.SteamAppListResponse;
import com.ecommerce.catalog_service.entity.CatalogEntity;
import com.ecommerce.catalog_service.repository.CatalogRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class SteamApiService {
    private final CatalogRepository catalogRepository;
    private final RestTemplate restTemplate = new RestTemplate();
    private final Environment env;
    private final KeyInventoryClient keyInventoryClient;

    @Transactional
    public List<SteamAppListResponse.SteamAppDto> fetchSteamApps() {
        String url = "https://api.steampowered.com/IStoreService/GetAppList/v1/?key="
                + env.getProperty("steam.key") + "&max_results=10";

        SteamAppListResponse response = restTemplate.getForObject(url, SteamAppListResponse.class);

        if (response == null || response.getResponse() == null) {
            return Collections.emptyList();
        }

        List<SteamAppListResponse.SteamAppDto> apps = response.getResponse().getApps();

        for (var appDto : apps) {
            fetchAndSaveAppDetail(String.valueOf(appDto.getAppId()));
            try { Thread.sleep(300); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
        }

        log.info("Fetching and Saving Steam Apps complete.");
        return apps;
    }

    private void fetchAndSaveAppDetail(String productId) {
        String detailUrl = "https://store.steampowered.com/api/appdetails?appids=" + productId + "&l=korean";

        Map<String, SteamAppListResponse.AppDetailData> result = restTemplate.exchange(
                detailUrl,
                org.springframework.http.HttpMethod.GET,
                null,
                new ParameterizedTypeReference<Map<String, SteamAppListResponse.AppDetailData>>() {}
        ).getBody();

        if (result == null || !result.containsKey(productId)) return;

        SteamAppListResponse.AppDetailData detail = result.get(productId);
        if (!detail.isSuccess() || detail.getData() == null) return;

        SteamAppListResponse.AppDetailData.Data apiData = detail.getData();

        int finalPrice = 0;
        if (apiData.getPriceOverview() != null) {
            finalPrice = apiData.getPriceOverview().getFinalValue() / 100;
        }
        String releaseDate = (apiData.getReleaseDate() != null) ? apiData.getReleaseDate().getDate() : "";

        long stock = keyInventoryClient.getKeys().stream().filter(key -> key.getProductId().equals(productId)).count();

        CatalogEntity foundEntity = catalogRepository.findByProductId(productId);
        if (foundEntity == null) {
            foundEntity = CatalogEntity.createCatalog(
                    productId,
                    apiData.getName(),
                    finalPrice,
                    (int) stock,
                    apiData.getHeaderImage(),
                    apiData.getDetailedDescription(),
                    releaseDate
            );
        } else {
            foundEntity.updateInfo(apiData.getName(), finalPrice, apiData.getDetailedDescription(), releaseDate);
        }

        final CatalogEntity entityToUpdate = foundEntity;

        entityToUpdate.clearCollections();

        if (apiData.getScreenshots() != null) {
            apiData.getScreenshots().forEach(s -> entityToUpdate.addImage(s.getPathFull()));
        }
        if (apiData.getGenres() != null) {
            apiData.getGenres().forEach(g -> entityToUpdate.addGenre(g.getDescription()));
        }
        if (apiData.getCategories() != null) {
            apiData.getCategories().forEach(c -> entityToUpdate.addCategory(c.getDescription()));
        }

        catalogRepository.save(entityToUpdate);
    }
}
