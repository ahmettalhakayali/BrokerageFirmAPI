package com.brokerage.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.brokerage.model.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
	Optional<Customer> findByUsername(String username);
}
