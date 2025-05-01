package com.clinic.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.clinic.common.dto.PersonRecord;

@FeignClient(name = "user-service", path = "/person")
public interface PersonClient {

	@GetMapping("/{personId}")
	PersonRecord getPersonById(@PathVariable Long personId);
}