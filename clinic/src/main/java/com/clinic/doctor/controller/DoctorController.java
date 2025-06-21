package com.clinic.doctor.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.clinic.doctor.dto.DoctorRecord;
import com.clinic.doctor.entity.Doctor;
import com.clinic.doctor.repository.DoctorRepository;
import com.clinic.doctor.service.DoctorService;
import com.clinic.patient.dto.PatientRecord;

@RestController
@RequestMapping(path = "/doctor")
public class DoctorController {

	@Autowired
	private DoctorService doctorService;

	@Autowired
	private DoctorRepository doctorRepository;

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

	@GetMapping("/by-person/{personId}")
	public ResponseEntity<Long> getPatientIdByPersonId(@PathVariable Long personId) {
		Doctor patient = doctorRepository.findByPersonId(personId);
		return ResponseEntity.ok(patient.getId());
	}

	@GetMapping("/patients/completed/{doctorId}")
	@PreAuthorize("hasRole('DOCTOR')")
	public ResponseEntity<List<PatientRecord>> getCompletedPatients(@PathVariable Long doctorId) {
		List<PatientRecord> patients = doctorService.getCompletedPatientsByDoctor(doctorId);
		return ResponseEntity.ok(patients);
	}

}
