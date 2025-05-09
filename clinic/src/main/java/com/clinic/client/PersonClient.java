package com.clinic.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.clinic.common.dto.PersonRecord;

@FeignClient(name = "user-service", path = "/person")
public interface PersonClient {

	@GetMapping
	PersonRecord getPersonById(@RequestParam Long personId);
}