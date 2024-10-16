package com.brokerage.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.brokerage.model.Asset;
import com.brokerage.model.Order;
import com.brokerage.repository.AssetRepository;
import com.brokerage.repository.OrderRepository;

public class OrderServiceTest {

	@Mock
	private AssetRepository assetRepository;

	@Mock
	private OrderRepository orderRepository;

	@InjectMocks
	private OrderService orderService;

	@BeforeEach
	public void setup() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	public void testCreateOrder_Success() throws Exception {
		String customerId = "1";
		String assetName = "Asset1";
		String side = "BUY";
		int size = 10;
		double price = 100.0;

		Asset asset = new Asset();
		asset.setUsableSize(20);
		asset.setId(1L);

		Order order = new Order();
		order.setCustomerId(Long.valueOf(customerId));
		order.setAssetName(assetName);
		order.setOrderSide(side);
		order.setSize(size);
		order.setPrice(price);
		order.setStatus("PENDING");
		order.setCreateDate(LocalDate.now());
		order.setId(1L);

		when(assetRepository.findByCustomerIdAndAssetName(Long.valueOf(customerId), assetName))
				.thenReturn(Optional.of(asset));

		when(orderRepository.save(any(Order.class))).thenReturn(order);

		Order result = orderService.createOrder(customerId, assetName, side, size, price);

		assertNotNull(result);
		assertEquals(order, result);
		verify(orderRepository, times(1)).save(any(Order.class));
	}

	@Test
	public void testListOrders() {
		String customerId = "1";
		LocalDate startDate = LocalDate.now().minusDays(1);
		LocalDate endDate = LocalDate.now();

		Order order1 = new Order();
		Order order2 = new Order();
		when(orderRepository.findByCustomerIdAndCreateDateBetween(Long.valueOf(customerId), startDate, endDate))
				.thenReturn(Arrays.asList(order1, order2));

		List<Order> orders = orderService.listOrders(customerId, startDate, endDate, java.util.Optional.empty(),
				java.util.Optional.empty());

		assertNotNull(orders);
		assertEquals(2, orders.size());
	}

	@Test
	public void testDeleteOrder_Success() throws Exception {
		String customerId = "1";
		Long orderId = 1L;

		Order order = new Order();
		order.setStatus("PENDING");
		order.setOrderSide("BUY");
		order.setAssetName("Asset1");
		order.setSize(10);

		when(orderRepository.findByIdAndCustomerId(orderId, Long.valueOf(customerId))).thenReturn(Optional.of(order));

		Asset asset = new Asset();
		asset.setUsableSize(50);

		when(assetRepository.findByCustomerIdAndAssetName(Long.valueOf(customerId), order.getAssetName()))
				.thenReturn(Optional.of(asset));

		orderService.deleteOrder(orderId, customerId);

		verify(orderRepository, times(1)).delete(order);
		verify(assetRepository, times(1)).save(asset);
		assertEquals(60, asset.getUsableSize());
	}

	@Test
	public void testDepositMoney_Success() throws Exception {
		Long customerId = 1L;
		String assetName = "TRY";
		double amount = 100;

		Asset asset = new Asset();
		asset.setUsableSize(50);

		when(assetRepository.findByCustomerIdAndAssetName(Long.valueOf(customerId), assetName))
				.thenReturn(Optional.of(asset));

		orderService.depositMoney(customerId, amount);

		assertEquals(150, asset.getUsableSize());
		verify(assetRepository, times(1)).save(asset);
	}

	@Test
	public void testWithdrawMoney_Success() throws Exception {
		Long customerId = 1L;
		String assetName = "TRY";
		double amount = 50;

		Asset asset = new Asset();
		asset.setUsableSize(100);

		when(assetRepository.findByCustomerIdAndAssetName(Long.valueOf(customerId), assetName))
				.thenReturn(Optional.of(asset));

		orderService.withdrawMoney(customerId, amount, assetName);

		assertEquals(50, asset.getUsableSize());
		verify(assetRepository, times(1)).save(asset);
	}

	@Test
	public void testListAssets() {
		Long customerId = 1L;
		Asset asset1 = new Asset();
		Asset asset2 = new Asset();

		when(assetRepository.findByCustomerId(Long.valueOf(customerId))).thenReturn(Arrays.asList(asset1, asset2));

		List<Asset> assets = orderService.listAssets(customerId);

		assertNotNull(assets);
		assertEquals(2, assets.size());
	}
}
