package com.brokerage.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.brokerage.model.Asset;
import com.brokerage.repository.AssetRepository;

@Configuration
public class AssetInitializationConfig {

	@Bean
	public CommandLineRunner initDatabase(AssetRepository assetRepository) {
		return args -> {
			if (assetRepository.count() == 0) {
				Asset asset1 = new Asset();
				asset1.setCustomerId(5L);
				asset1.setAssetName("Gold");
				asset1.setSize(100);
				asset1.setUsableSize(80);

				Asset asset2 = new Asset();
				asset2.setCustomerId(6L);
				asset2.setAssetName("Silver");
				asset2.setSize(200);
				asset2.setUsableSize(150);

				Asset asset3 = new Asset();
				asset3.setCustomerId(7L);
				asset3.setAssetName("Platinum");
				asset3.setSize(50);
				asset3.setUsableSize(45);

				Asset asset4 = new Asset();
				asset4.setCustomerId(8L);
				asset4.setAssetName("Diamond");
				asset4.setSize(10);
				asset4.setUsableSize(10);

				assetRepository.save(asset1);
				assetRepository.save(asset2);
				assetRepository.save(asset3);
				assetRepository.save(asset4);

				System.out.println("Initialized Asset table with default values.");
			}
		};
	}
}
