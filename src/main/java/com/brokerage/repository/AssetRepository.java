package com.brokerage.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.brokerage.model.Asset;

public interface AssetRepository extends JpaRepository<Asset, Long> {
	List<Asset> findByCustomerId(Long customerId);

	Optional<Asset> findByCustomerIdAndAssetName(Long customerId, String assetName);

}
