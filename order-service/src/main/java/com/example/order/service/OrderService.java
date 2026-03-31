package com.example.order.service;

import com.example.order.dto.request.OrderRequest;
import com.example.order.dto.response.OrderResponse;

public interface OrderService {
    OrderResponse createOrder(OrderRequest request, String jwtToken);
    OrderResponse cancelOrder(Long orderId);
    OrderResponse getOrder(Long orderId);
}
