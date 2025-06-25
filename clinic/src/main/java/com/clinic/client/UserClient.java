package com.clinic.client;

import java.util.Map;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "user-service", path = "/api/auth")
public interface UserClient {

	@GetMapping("/getEmailAndUserIdByPersonId")
	Map<String, Object> getEmailAndUserIdByPersonId(@RequestParam("personId") Long personId);

}
