package com.ecommerce.catalog_service.controller;

import com.ecommerce.catalog_service.service.SteamDataBulkLoader;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/catalog/admin")
@RequiredArgsConstructor
@Slf4j
public class AdminCatalogController {

    private final SteamDataBulkLoader steamDataBulkLoader;

    @GetMapping("/load-steam")
    public ResponseEntity<String> loadSteamData() {
        log.info("[Admin] Steam 데이터 벌크 로딩 시작...");

        steamDataBulkLoader.runHeavyLoad();

        return ResponseEntity.ok("10만 건 데이터 로딩이 완료되었습니다. 로그를 확인하세요.");
    }
}
