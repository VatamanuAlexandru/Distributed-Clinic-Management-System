package com.clinic.patient.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.clinic.patient.entity.Patient;
import com.clinic.patient.repository.PatientRepository;
import com.clinic.patient.service.PatientService;

@RestController
@RequestMapping("/patient")
public class PatientController {

	@Autowired
	private PatientService patientService;

	@Autowired
	private PatientRepository patientRepository;

	@PostMapping
	public ResponseEntity<?> createPatient(@RequestParam Long personId) {
		patientService.createPatient(personId);
		return ResponseEntity.ok().build();
	}

	@GetMapping("/by-person/{personId}")
	public ResponseEntity<Long> getPatientIdByPersonId(@PathVariable Long personId) {
		Patient patient = patientRepository.findByPersonId(personId);
		return ResponseEntity.ok(patient.getId());
	}

}
