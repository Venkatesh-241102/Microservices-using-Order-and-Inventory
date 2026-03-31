package com.example.order.client;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class InventoryClient {

    private final RestTemplate restTemplate;

    @Value("${inventory.service.url}")
    private String inventoryServiceUrl;

    @CircuitBreaker(name = "inventoryService", fallbackMethod = "deductStockFallback")
    @Retry(name = "inventoryService")
    public boolean deductStock(Long productId, Integer quantity, String token) {
        String url = inventoryServiceUrl + "/api/inventory/deduct";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + token); // Pass the JWT token

        Map<String, Object> body = new HashMap<>();
        body.put("productId", productId);
        body.put("quantity", quantity);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);

        try {
            // We only care if it doesn't throw a 400 Bad Request
            restTemplate.postForEntity(url, request, Object.class);
            return true;
        } catch (Exception e) {
            log.error("Failed to deduct stock: {}", e.getMessage());
            return false;
        }
    }

    public boolean deductStockFallback(Long productId, Integer quantity, String token, Exception e) {
        log.error("Circuit breaker active. Deduct stock fallback for productId {}: {}", productId, e.getMessage());
        return false;
    }
}
