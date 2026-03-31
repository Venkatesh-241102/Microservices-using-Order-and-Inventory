package com.example.inventory.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class InventoryResponse {
    private Long productId;
    private Integer quantity;
    private boolean isAvailable;
}
