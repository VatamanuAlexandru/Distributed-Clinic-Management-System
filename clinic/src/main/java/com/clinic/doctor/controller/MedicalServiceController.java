package com.clinic.doctor.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.clinic.doctor.dto.MedicalServiceRecord;
import com.clinic.doctor.service.MedicalServiceService;

@RestController
@RequestMapping("/services")
public class MedicalServiceController {

	@Autowired
	private MedicalServiceService medicalServiceService;

	@GetMapping
	public ResponseEntity<List<MedicalServiceRecord>> getAllServices(
			@RequestParam(required = false) Long departmentId) {
		if (departmentId != null) {
			return ResponseEntity.ok(medicalServiceService.getServicesByDepartmentId(departmentId));
		}
		return ResponseEntity.ok(medicalServiceService.getAllServices());
	}

}
