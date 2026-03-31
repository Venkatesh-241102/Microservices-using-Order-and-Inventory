package com.example.order.service.impl;

import com.example.order.client.InventoryClient;
import com.example.order.dto.request.OrderRequest;
import com.example.order.dto.response.OrderResponse;
import com.example.order.entity.Order;
import com.example.order.repository.OrderRepository;
import com.example.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final InventoryClient inventoryClient;

    @Override
    public OrderResponse createOrder(OrderRequest request, String jwtToken) {
        // Initially in CREATED state
        Order order = Order.builder()
                .productId(request.getProductId())
                .quantity(request.getQuantity())
                .status("CREATED")
                .orderDate(LocalDateTime.now())
                .build();
        
        order = orderRepository.save(order);

        // Deduct stock
        boolean stockDeducted = inventoryClient.deductStock(request.getProductId(), request.getQuantity(), jwtToken);

        if (stockDeducted) {
            order.setStatus("CONFIRMED");
        } else {
            order.setStatus("CANCELLED");
        }
        
        order = orderRepository.save(order);
        
        if (order.getStatus().equals("CANCELLED")) {
            throw new RuntimeException("Order Cancelled due to insufficient stock or Inventory Service unavailability");
        }
        
        return mapToResponse(order);
    }

    @Override
    public OrderResponse cancelOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        order.setStatus("CANCELLED");
        order = orderRepository.save(order);
        // ideally add logic to return stock to inventory via queue or rest
        return mapToResponse(order);
    }

    @Override
    public OrderResponse getOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        return mapToResponse(order);
    }

    private OrderResponse mapToResponse(Order order) {
        return OrderResponse.builder()
                .id(order.getId())
                .productId(order.getProductId())
                .quantity(order.getQuantity())
                .status(order.getStatus())
                .orderDate(order.getOrderDate())
                .build();
    }
}
