package com.example.orderserivce.service;

import com.example.commondto.dto.OrderEvent;
import com.example.orderserivce.model.Order;
import com.example.orderserivce.repo.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;

    private final OrderProducer orderProducer;

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public Order createOrder(Order order) {
        Order savedOrder = orderRepository.save(order);
        OrderEvent event = OrderEvent.builder()
                .orderId(savedOrder.getId().toString())
                .amount(savedOrder.getPrice())
                .build();
        orderProducer.sendOrderEvent(event);
        return savedOrder;
    }

    public Order getOrderById(UUID id) {
        return orderRepository.findById(id).orElseThrow();
    }
}