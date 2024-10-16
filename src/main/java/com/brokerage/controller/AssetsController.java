package com.brokerage.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.brokerage.model.Asset;
import com.brokerage.requests.ListAssetsRequest;
import com.brokerage.requests.MoneyRequest;
import com.brokerage.service.OrderService;

@RestController
@RequestMapping("/assets")
public class AssetsController {

	@Autowired
	private OrderService orderService;

	@PostMapping("/deposit")
	public void depositMoney(@RequestBody MoneyRequest moneyRequest) {
		try {
			orderService.depositMoney(moneyRequest.getCustomerId(), moneyRequest.getAmount());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@PostMapping("/withdraw")
	public void withdrawMoney(@RequestBody MoneyRequest moneyRequest) {
		try {
			orderService.withdrawMoney(moneyRequest.getCustomerId(), moneyRequest.getAmount(), moneyRequest.getIban());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@PostMapping("/list")
	public List<Asset> listAssets(@RequestBody ListAssetsRequest listAssetsRequest) {
		return orderService.listAssets(listAssetsRequest.getCustomerId());
	}
}
