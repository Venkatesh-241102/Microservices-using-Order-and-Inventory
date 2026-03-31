package com.example.inventory.service;

import com.example.inventory.dto.request.InventoryRequest;
import com.example.inventory.dto.response.InventoryResponse;

public interface InventoryService {
    InventoryResponse getInventory(Long productId);
    InventoryResponse checkAvailability(Long productId, Integer quantity);
    InventoryResponse addStock(InventoryRequest request);
    InventoryResponse deductStock(InventoryRequest request);
}
