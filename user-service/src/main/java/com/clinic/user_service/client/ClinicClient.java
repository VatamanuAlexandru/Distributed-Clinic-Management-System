package com.clinic.user_service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "clinic", path = "/")
public interface ClinicClient {

	@PostMapping("/doctor")
	void createDoctor(@RequestParam Long personId);

	@GetMapping("/doctor/by-person/{personId}")
	Long getDoctorIdByPersonId(@PathVariable("personId") Long personId);

	@PostMapping("/patient")
	void createPatient(@RequestParam Long personId);

	@GetMapping("/patient/by-person/{personId}")
	Long getPatientIdByPersonId(@PathVariable("personId") Long personId);
}
