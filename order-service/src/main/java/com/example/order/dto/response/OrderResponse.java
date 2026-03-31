package com.example.order.dto.response;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Builder
public class OrderResponse {
    private Long id;
    private Long productId;
    private Integer quantity;
    private String status;
    private LocalDateTime orderDate;
}
