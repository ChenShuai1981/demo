package com.example.demo.service;

import com.example.demo.entity.Order;
import org.springframework.stereotype.Service;

@Service
public class OrderService {
    public Order getOrderById(Integer id) {
        return new Order(id);
    }
}
