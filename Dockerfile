FROM eclipse-temurin:21-jdk-jammy
VOLUME /tmp
COPY build/libs/catalog_service-1.0.jar CatalogServer.jar
ENTRYPOINT ["java", "-jar", "CatalogServer.jar"]