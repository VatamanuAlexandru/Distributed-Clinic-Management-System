package com.clinic.patient.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.clinic.patient.dto.TreatmentRecord;
import com.clinic.patient.service.TreatmentService;

@RestController
@RequestMapping("/treatment")
public class TreatmentController {

	@Autowired
	private TreatmentService treatmentService;

	@GetMapping("/patient/{patientId}")
	public ResponseEntity<List<TreatmentRecord>> getTreatmentsForPatient(@PathVariable Long patientId) {
		return ResponseEntity.ok(treatmentService.getTreatmentsForPatient(patientId));
	}

	@GetMapping("/{treatmentId}")
	public ResponseEntity<TreatmentRecord> getTreatmentById(@PathVariable Long treatmentId) {
		return ResponseEntity.ok(treatmentService.getTreatmentById(treatmentId));
	}

	@PostMapping("/diagnosis/{diagnosisId}")
	public ResponseEntity<TreatmentRecord> createTreatment(@PathVariable Long diagnosisId,
			@RequestBody TreatmentRecord dto) {
		return ResponseEntity.ok(treatmentService.createTreatment(diagnosisId, dto));
	}

	@PutMapping("/{treatmentId}")
	public ResponseEntity<TreatmentRecord> updateTreatment(@PathVariable Long treatmentId,
			@RequestBody TreatmentRecord dto) {
		return ResponseEntity.ok(treatmentService.updateTreatment(treatmentId, dto));
	}

	@DeleteMapping("/{treatmentId}")
	public ResponseEntity<?> deleteTreatment(@PathVariable Long treatmentId) {
		treatmentService.deleteTreatment(treatmentId);
		return ResponseEntity.noContent().build();
	}
}
