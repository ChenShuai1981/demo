package com.example.demo.controller;

import com.example.demo.dto.MyException;
import com.example.demo.config.ResponseResult;
import com.example.demo.dto.ResultCode;
import com.example.demo.entity.Order;
import com.example.demo.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/orders")
@ResponseResult
public class OrderController {

    @Autowired
    private OrderService orderService;

    @GetMapping("{id}")
    public Order getOrder(@PathVariable("id") Integer id) {
        if (id < 0) {
            throw new MyException(ResultCode.INVALID_USER_ID);
        }
        Order order = orderService.getOrderById(id);
        return order;
    }
}
