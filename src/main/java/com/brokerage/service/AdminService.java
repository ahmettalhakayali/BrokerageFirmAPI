package com.brokerage.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.brokerage.model.Asset;
import com.brokerage.model.Order;
import com.brokerage.repository.AssetRepository;
import com.brokerage.repository.OrderRepository;

@Service
public class AdminService {

	@Autowired
	private OrderRepository orderRepository;

	@Autowired
	private AssetRepository assetRepository;

	public void matchPendingOrders() {
		List<Order> pendingOrders = orderRepository.findByStatus("PENDING");

		for (Order order : pendingOrders) {
			Asset asset = assetRepository.findById(order.getId()).orElseThrow();
			asset.setSize(asset.getSize() - order.getSize());
			asset.setUsableSize(asset.getUsableSize() - order.getSize());
			assetRepository.save(asset);
		}

		pendingOrders.forEach(order -> {
			order.setStatus("MATCHED");
			orderRepository.save(order);
		});
	}
}
