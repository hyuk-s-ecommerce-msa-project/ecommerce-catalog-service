package com.ecommerce.catalog_service.service;

import com.ecommerce.catalog_service.entity.CatalogEntity;
import com.ecommerce.catalog_service.repository.CatalogRepository;
import com.ecommerce.snowflake.util.SnowflakeIdGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class SteamDataBulkLoader {

    private final CatalogRepository catalogRepository;
    private final SnowflakeIdGenerator snowflakeIdGenerator;

    public void runHeavyLoad() {
        String filePath = "C:/Users/Hyuk/Desktop/Project/steam_data_output.csv";
        loadFromCsv(filePath);
    }

    private void loadFromCsv(String path) {
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(new FileInputStream(path), StandardCharsets.UTF_8))) {

            String line;
            List<CatalogEntity> batchList = new ArrayList<>();
            int count = 0;

            br.readLine();

            while ((line = br.readLine()) != null) {
                String[] data = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");

                if (data.length < 6) continue; // 최소 데이터 미달 시 스킵

                // 3. 엔티티 생성 및 데이터 매핑
                CatalogEntity entity = mapToEntity(data);
                batchList.add(entity);
                count++;

                if (count % 1000 == 0) {
                    catalogRepository.saveAll(batchList);
                    batchList.clear();
                    log.info("[Data Load] {} records saved to DB...", count);
                }
            }

            if (!batchList.isEmpty()) {
                catalogRepository.saveAll(batchList);
            }

            log.info("[Data Load] Finished! Total {} apps loaded successfully.", count);

        } catch (FileNotFoundException e) {
            log.error("파일을 찾을 수 없습니다. 경로를 확인하세요: {}", path);
        } catch (IOException e) {
            log.error("파일 읽기 중 오류 발생: ", e);
        }
    }

    private CatalogEntity mapToEntity(String[] data) {
        Long id = snowflakeIdGenerator.nextId();
        String productId = data[0].replace("\"", "").trim();
        String name = data[1].replace("\"", "").trim();

        int price = 0;
        try {
            price = Integer.parseInt(data[3].replace("\"", "").trim());
        } catch (NumberFormatException e) {
            log.warn("Price format error for app: {}", productId);
        }

        String releaseDate = data[2].replace("\"", "").trim();
        String detailDescription = data[4].replace("\"", "").trim();
        String headerImage = data[5].replace("\"", "").trim();

        CatalogEntity entity = CatalogEntity.createCatalog(
                id,
                productId,
                name,
                price,
                100,
                headerImage,
                detailDescription,
                releaseDate
        );

        if (data.length > 6 && data[6] != null && !data[6].isEmpty()) {
            String categoriesRaw = data[6].replace("\"", "").trim();
            for (String cat : categoriesRaw.split(";")) {
                if (!cat.isBlank()) {
                    entity.addCategory(snowflakeIdGenerator.nextId(), cat.trim());
                }
            }
        }

        if (data.length > 7 && data[7] != null && !data[7].isEmpty()) {
            String genresRaw = data[7].replace("\"", "").trim();
            for (String genre : genresRaw.split(";")) {
                if (!genre.isBlank()) {
                    entity.addGenre(snowflakeIdGenerator.nextId(), genre.trim());
                }
            }
        }

        return entity;
    }
}
