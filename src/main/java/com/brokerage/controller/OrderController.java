package com.brokerage.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.brokerage.model.Order;
import com.brokerage.requests.DeleteOrderRequest;
import com.brokerage.requests.ListOrdersRequest;
import com.brokerage.requests.OrderRequest;
import com.brokerage.service.OrderService;

@RestController
@RequestMapping("/orders")
public class OrderController {

	@Autowired
	private OrderService orderService;

	@PostMapping("/create")
	public Order createOrder(@RequestBody OrderRequest request) throws Exception {
		return orderService.createOrder(request.getCustomerId(), request.getAssetName(), request.getSide(),
				request.getSize(), request.getPrice());
	}

	@PostMapping("/list")
	public List<Order> listOrders(@RequestBody ListOrdersRequest request) {
		return orderService.listOrders(request.getCustomerId(), LocalDate.parse(request.getStartDate()),
				LocalDate.parse(request.getEndDate()), Optional.ofNullable(request.getStatus()),
				Optional.ofNullable(request.getSide()));
	}

	@PostMapping("/delete")
	public void deleteOrder(@RequestBody DeleteOrderRequest request) throws Exception {
		orderService.deleteOrder(request.getOrderId(), request.getCustomerId());
	}
}
