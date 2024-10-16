package com.brokerage.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.brokerage.model.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {
	List<Order> findByCustomerIdAndCreateDateBetween(Long customerId, LocalDate startDate, LocalDate endDate);

	Optional<Order> findByIdAndCustomerId(Long orderId, Long customerId);

	List<Order> findByStatus(String string);
}
