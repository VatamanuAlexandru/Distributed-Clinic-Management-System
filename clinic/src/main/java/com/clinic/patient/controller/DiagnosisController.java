package com.clinic.patient.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.clinic.patient.dto.DiagnosisRecord;
import com.clinic.patient.service.DiagnosisService;

@RestController
@RequestMapping("/diagnosis")
public class DiagnosisController {

	@Autowired
	private DiagnosisService diagnosisService;

	@GetMapping("/patient/{patientId}")
	public ResponseEntity<List<DiagnosisRecord>> getDiagnosesByPatient(@PathVariable Long patientId) {
		return ResponseEntity.ok(diagnosisService.getDiagnosesForPatient(patientId));
	}

	@PostMapping("/patient/{patientId}")
	public ResponseEntity<DiagnosisRecord> addDiagnosis(@PathVariable Long patientId,
			@RequestBody DiagnosisRecord dto) {
		return ResponseEntity.ok(diagnosisService.createDiagnosis(patientId, dto));
	}

	@PutMapping("/patient/{patientId}/{diagnosisId}")
	public ResponseEntity<DiagnosisRecord> updateDiagnosis(@PathVariable Long patientId, @PathVariable Long diagnosisId,
			@RequestBody DiagnosisRecord dto) {
		return ResponseEntity.ok(diagnosisService.updateDiagnosis(patientId, diagnosisId, dto));
	}

	@DeleteMapping("/patient/{patientId}/{diagnosisId}")
	public ResponseEntity<?> deleteDiagnosis(@PathVariable Long patientId, @PathVariable Long diagnosisId) {
		diagnosisService.deleteDiagnosis(patientId, diagnosisId);
		return ResponseEntity.noContent().build();
	}
}
