package com.clinic.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.clinic.common.dto.NotificationRequest;

@FeignClient(name = "notification", path = "/api/notification")
public interface NotificationClient {

	@PostMapping
	void notify(@RequestBody NotificationRequest request);
}
