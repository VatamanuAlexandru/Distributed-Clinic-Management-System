package com.clinic.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.clinic.common.dto.CreatePaymentObligationRequest;

@FeignClient(name = "payment", path = "/api/payment-obligations")
public interface PaymentClient {

	@PostMapping
	void createPaymentObligation(@RequestBody CreatePaymentObligationRequest request);
}
