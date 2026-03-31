package com.example.inventory.service.impl;

import com.example.inventory.dto.request.InventoryRequest;
import com.example.inventory.dto.response.InventoryResponse;
import com.example.inventory.entity.Inventory;
import com.example.inventory.repository.InventoryRepository;
import com.example.inventory.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class InventoryServiceImpl implements InventoryService {

    private final InventoryRepository inventoryRepository;

    @Override
    public InventoryResponse getInventory(Long productId) {
        Inventory inventory = inventoryRepository.findByProductId(productId)
                .orElse(Inventory.builder().productId(productId).quantity(0).build());
        return mapToResponse(inventory, inventory.getQuantity() > 0);
    }

    @Override
    public InventoryResponse checkAvailability(Long productId, Integer quantity) {
        Inventory inventory = inventoryRepository.findByProductId(productId)
                .orElse(Inventory.builder().productId(productId).quantity(0).build());
        boolean isAvailable = inventory.getQuantity() >= quantity;
        return mapToResponse(inventory, isAvailable);
    }

    @Override
    public InventoryResponse addStock(InventoryRequest request) {
        Optional<Inventory> optInv = inventoryRepository.findByProductId(request.getProductId());
        Inventory inventory;
        if (optInv.isPresent()) {
            inventory = optInv.get();
            inventory.setQuantity(inventory.getQuantity() + request.getQuantity());
        } else {
            inventory = Inventory.builder()
                    .productId(request.getProductId())
                    .quantity(request.getQuantity())
                    .build();
        }
        inventoryRepository.save(inventory);
        return mapToResponse(inventory, true);
    }

    @Override
    public InventoryResponse deductStock(InventoryRequest request) {
        Inventory inventory = inventoryRepository.findByProductId(request.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found in inventory"));

        if (inventory.getQuantity() < request.getQuantity()) {
            throw new RuntimeException("Insufficient stock");
        }

        inventory.setQuantity(inventory.getQuantity() - request.getQuantity());
        inventoryRepository.save(inventory);
        return mapToResponse(inventory, inventory.getQuantity() > 0);
    }

    private InventoryResponse mapToResponse(Inventory inventory, boolean available) {
        return InventoryResponse.builder()
                .productId(inventory.getProductId())
                .quantity(inventory.getQuantity())
                .isAvailable(available)
                .build();
    }
}
