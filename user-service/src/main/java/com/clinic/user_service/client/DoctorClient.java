package com.clinic.user_service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "clinic", path = "/doctor")
public interface DoctorClient {

	@PostMapping
	void createDoctor(@RequestParam Long personId);

	@GetMapping("/by-person/{personId}")
	Long getDoctorIdByPersonId(@PathVariable("personId") Long personId);

}
