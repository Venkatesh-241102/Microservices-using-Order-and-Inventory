package com.example.inventory.controller;

import com.example.inventory.dto.request.InventoryRequest;
import com.example.inventory.dto.response.InventoryResponse;
import com.example.inventory.service.InventoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/inventory")
@RequiredArgsConstructor
public class InventoryController {

    private final InventoryService inventoryService;

    @GetMapping("/{productId}")
    public ResponseEntity<InventoryResponse> getByProductId(@PathVariable Long productId) {
        return ResponseEntity.ok(inventoryService.getInventory(productId));
    }

    @GetMapping("/check")
    public ResponseEntity<InventoryResponse> checkAvailability(
            @RequestParam Long productId,
            @RequestParam Integer quantity) {
        return ResponseEntity.ok(inventoryService.checkAvailability(productId, quantity));
    }

    @PostMapping("/add")
    public ResponseEntity<InventoryResponse> addStock(@Valid @RequestBody InventoryRequest request) {
        return ResponseEntity.ok(inventoryService.addStock(request));
    }

    @PostMapping("/deduct")
    public ResponseEntity<InventoryResponse> deductStock(@Valid @RequestBody InventoryRequest request) {
        return ResponseEntity.ok(inventoryService.deductStock(request));
    }
}
