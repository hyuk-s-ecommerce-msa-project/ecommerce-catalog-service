package com.ecommerce.catalog_service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Data
public class SteamAppListResponse {
    @JsonProperty("response")
    private AppList response;

    @Data
    public static class AppList {
        private List<SteamAppDto> apps;
    }

    @Data
    public static class SteamAppDto {
        @JsonProperty("appid")
        private Integer appId;
        private String name;
    }

    @Data
    public static class AppDetailData {
        private boolean success;
        private Data data;

        @Getter
        @Setter
        public static class Data {
            private String name;
            @JsonProperty("detailed_description")
            private String detailedDescription;
            @JsonProperty("price_overview")
            private PriceOverview priceOverview;
            @JsonProperty("release_date")
            private ReleaseDate releaseDate;
            private List<Screenshot> screenshots;
            private List<Genre> genres;
            private List<Category> categories;
        }

        @Getter
        @Setter
        public static class PriceOverview {
            private Integer final_price;
            @JsonProperty("final")
            private Integer finalValue;
        }

        @Getter
        @Setter
        public static class ReleaseDate {
            private String date;
        }

        @Getter
        @Setter
        public static class Screenshot {
            @JsonProperty("path_full")
            private String pathFull;
        }

        @Getter
        @Setter
        public static class Genre {
            private String description;
        }

        @Getter
        @Setter
        public static class Category {
            private String description;
        }
    }
}
