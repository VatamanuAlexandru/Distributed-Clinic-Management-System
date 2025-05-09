package com.clinic.doctor.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.clinic.doctor.dto.DoctorRecord;
import com.clinic.doctor.service.DoctorService;

@RestController
@RequestMapping(path = "/doctor")
public class DoctorController {

	@Autowired
	private DoctorService doctorService;

	@PostMapping
	public ResponseEntity<?> createDoctor(@RequestParam Long personId) {
		doctorService.createDoctor(personId);
		return ResponseEntity.ok().build();
	}

	@GetMapping("/{departmentId}")
	public ResponseEntity<?> getDoctorsByDepartmentId(@PathVariable Long departmentId) {
		List<DoctorRecord> doctorRecords = doctorService.getDoctorsByDepartmentId(departmentId);
		return ResponseEntity.ok(doctorRecords);
	}

	@GetMapping("/get/{id}")
	public ResponseEntity<?> getDoctorById(@PathVariable Long id) {
		DoctorRecord doctorRecord = doctorService.getDoctorById(id);
		return ResponseEntity.ok(doctorRecord);
	}

	@GetMapping("/by-service/{serviceId}")
	public ResponseEntity<List<DoctorRecord>> getDoctorByServiceId(@PathVariable Long serviceId) {
		return ResponseEntity.ok(doctorService.getDoctorByServiceId(serviceId));
	}

}
