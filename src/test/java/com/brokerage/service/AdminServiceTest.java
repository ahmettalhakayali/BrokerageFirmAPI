package com.brokerage.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
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

public class AdminServiceTest {

	@Mock
	private OrderRepository orderRepository;

	@Mock
	private AssetRepository assetRepository;

	@InjectMocks
	private AdminService adminService;

	@BeforeEach
	public void setup() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	public void testMatchPendingOrders() {
		// Arrange
		Order order1 = new Order();
		order1.setId(1L);
		order1.setSize(10);

		Order order2 = new Order();
		order2.setId(2L);
		order2.setSize(5);

		when(orderRepository.findByStatus("PENDING")).thenReturn(Arrays.asList(order1, order2));

		Asset asset1 = new Asset();
		asset1.setSize(100);
		asset1.setUsableSize(100);

		Asset asset2 = new Asset();
		asset2.setSize(50);
		asset2.setUsableSize(50);

		when(assetRepository.findById(1L)).thenReturn(Optional.of(asset1));
		when(assetRepository.findById(2L)).thenReturn(Optional.of(asset2));

		// Act
		adminService.matchPendingOrders();

		// Assert
		verify(orderRepository, times(1)).findByStatus("PENDING");
		verify(assetRepository, times(1)).save(asset1);
		verify(assetRepository, times(1)).save(asset2);
		assertEquals(90, asset1.getSize());
		assertEquals(90, asset1.getUsableSize());
		assertEquals(45, asset2.getSize());
		assertEquals(45, asset2.getUsableSize());
	}
}
