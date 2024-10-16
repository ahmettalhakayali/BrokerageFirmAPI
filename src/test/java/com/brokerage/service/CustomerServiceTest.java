package com.brokerage.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.brokerage.model.Customer;
import com.brokerage.repository.CustomerRepository;

public class CustomerServiceTest {

	@Mock
	private CustomerRepository customerRepository;

	@InjectMocks
	private CustomerService customerService;

	@BeforeEach
	public void setup() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	public void testLoadUserByUsername_Success() {
		// Arrange
		String username = "testuser";
		Customer customer = new Customer();
		customer.setUsername(username);
		customer.setPassword("password");
		customer.setRole("USER");

		when(customerRepository.findByUsername(username)).thenReturn(Optional.of(customer));

		// Act
		UserDetails userDetails = customerService.loadUserByUsername(username);

		// Assert
		assertNotNull(userDetails);
		assertEquals(username, userDetails.getUsername());
		assertEquals("password", userDetails.getPassword());
		verify(customerRepository, times(1)).findByUsername(username);
	}

	@Test
	public void testLoadUserByUsername_NotFound() {
		// Arrange
		String username = "nonexistentuser";

		when(customerRepository.findByUsername(username)).thenReturn(Optional.empty());

		// Act & Assert
		Exception exception = assertThrows(UsernameNotFoundException.class, () -> {
			customerService.loadUserByUsername(username);
		});

		assertEquals("User not found: nonexistentuser", exception.getMessage());
		verify(customerRepository, times(1)).findByUsername(username);
	}
}
