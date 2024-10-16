package com.brokerage.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.brokerage.model.Asset;
import com.brokerage.model.Order;
import com.brokerage.repository.AssetRepository;
import com.brokerage.repository.OrderRepository;

@Service
public class OrderService {

	@Autowired
	private AssetRepository assetRepository;

	@Autowired
	private OrderRepository orderRepository;

	public Order createOrder(String customerId, String assetName, String side, int size, double price)
			throws Exception {
		Optional<Asset> assetOpt = assetRepository.findByCustomerIdAndAssetName(Long.valueOf(customerId), assetName);
		if (assetOpt.isPresent()) {
			Asset asset = assetOpt.get();
			if (side.equalsIgnoreCase("BUY") && asset.getUsableSize() < size) {
				throw new Exception("Insufficient TRY for the order.");
			}
			if (side.equalsIgnoreCase("SELL") && asset.getUsableSize() < size) {
				throw new Exception("Insufficient asset size for the order.");
			}

			Order order = new Order();
			order.setCustomerId(Long.valueOf(customerId));
			order.setAssetName(assetName);
			order.setOrderSide(side);
			order.setSize(size);
			order.setPrice(price);
			order.setStatus("PENDING");
			order.setCreateDate(LocalDate.now());

			Order savedOrder = orderRepository.save(order);

			if (side.equalsIgnoreCase("BUY")) {
				asset.setUsableSize(asset.getUsableSize() - size);
			} else if (side.equalsIgnoreCase("SELL")) {
				asset.setUsableSize(asset.getUsableSize() + size);
			}
			assetRepository.save(asset);

			System.out.println("Order Created");
			return savedOrder;
		} else {
			throw new Exception("Asset not found.");
		}
	}

	public List<Order> listOrders(String customerId, LocalDate startDate, LocalDate endDate, Optional<String> status,
			Optional<String> side) {

		List<Order> orders = orderRepository.findByCustomerIdAndCreateDateBetween(Long.valueOf(customerId), startDate,
				endDate);

		if (!orders.isEmpty()) {
			System.out.println("Orders are listed");
		} else {
			System.out.println("No Orders found");
		}

		return orders;

	}

	public void deleteOrder(Long orderId, String customerId) throws Exception {
		Optional<Order> orderOpt = orderRepository.findByIdAndCustomerId(orderId, Long.valueOf(customerId));
		if (orderOpt.isPresent() && orderOpt.get().getStatus().equalsIgnoreCase("PENDING")) {
			Order order = orderOpt.get();
			Optional<Asset> assetOpt = assetRepository.findByCustomerIdAndAssetName(Long.valueOf(customerId),
					order.getAssetName());
			if (assetOpt.isPresent()) {
				Asset asset = assetOpt.get();
				if (order.getOrderSide().equalsIgnoreCase("BUY")) {
					asset.setUsableSize(asset.getUsableSize() + order.getSize());
				} else if (order.getOrderSide().equalsIgnoreCase("SELL")) {
					asset.setUsableSize(asset.getUsableSize() - order.getSize());
				}
				assetRepository.save(asset);
			}
			orderRepository.delete(order);
		} else {
			throw new Exception("Cannot delete non-pending order.");
		}
	}

	public void depositMoney(Long customerId, double amount) throws Exception {
		Optional<Asset> assetOpt = assetRepository.findByCustomerIdAndAssetName(customerId, "TRY");

		if (assetOpt.isPresent()) {
			Asset asset = assetOpt.get();
			asset.setUsableSize(asset.getUsableSize() + (int) amount);
			assetRepository.save(asset);
			System.out.println("Deposit successful for customer ID: " + customerId);
		} else {
			throw new Exception("TRY asset not found for customer ID: " + customerId);
		}
	}

	public void withdrawMoney(Long customerId, double amount, String iban) throws Exception {
		Optional<Asset> tryAssetOpt = assetRepository.findByCustomerIdAndAssetName(customerId, "TRY");

		if (tryAssetOpt.isPresent()) {
			Asset tryAsset = tryAssetOpt.get();
			if (tryAsset.getUsableSize() >= amount) {
				tryAsset.setUsableSize(tryAsset.getUsableSize() - (int) amount);
				assetRepository.save(tryAsset);
				System.out.println("Withdrawal Successful. TRY balance updated.");
			} else {
				throw new Exception("Insufficient TRY for the withdrawal.");
			}
		} else {
			throw new Exception("Customer does not have a TRY asset.");
		}
	}

	public List<Asset> listAssets(Long customerId) {
		List<Asset> assets = assetRepository.findByCustomerId(customerId);
		if (!assets.isEmpty()) {
			System.out.println("Assets listed.");
		} else {
			System.out.println("No assets found.");
		}
		return assets;
	}

}