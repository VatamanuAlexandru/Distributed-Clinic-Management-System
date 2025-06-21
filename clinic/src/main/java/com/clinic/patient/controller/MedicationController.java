package com.clinic.patient.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.clinic.patient.dto.MedicationRecord;
import com.clinic.patient.service.MedicationService;

@RestController
@RequestMapping("/medication")
public class MedicationController {

	@Autowired
	private MedicationService medicationService;

	@GetMapping("/treatment/{treatmentId}")
	public ResponseEntity<List<MedicationRecord>> getMedicationsByTreatment(@PathVariable Long treatmentId) {
		return ResponseEntity.ok(medicationService.getMedicationsByTreatment(treatmentId));
	}

	@PostMapping("/treatment/{treatmentId}")
	public ResponseEntity<MedicationRecord> addMedication(@PathVariable Long treatmentId,
			@RequestBody MedicationRecord dto) {
		return ResponseEntity.ok(medicationService.addMedication(treatmentId, dto));
	}

	@PutMapping("/{medId}")
	public ResponseEntity<MedicationRecord> updateMedication(@PathVariable Long medId,
			@RequestBody MedicationRecord dto) {
		return ResponseEntity.ok(medicationService.updateMedication(medId, dto));
	}

	@DeleteMapping("/{medId}")
	public ResponseEntity<?> deleteMedication(@PathVariable Long medId) {
		medicationService.deleteMedication(medId);
		return ResponseEntity.noContent().build();
	}
}
