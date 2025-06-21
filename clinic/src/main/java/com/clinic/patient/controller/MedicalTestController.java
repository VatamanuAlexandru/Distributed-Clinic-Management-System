package com.clinic.patient.controller;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.clinic.patient.dto.MedicalTestRecord;
import com.clinic.patient.service.MedicalTestService;

@RestController
@RequestMapping("/patients/{patientId}/tests")
public class MedicalTestController {

	@Autowired
	private MedicalTestService testService;

	@GetMapping
	public List<MedicalTestRecord> list(@PathVariable Long patientId) {
		return testService.listByPatientRecord(patientId);
	}

	@GetMapping("/{id}")
	public ResponseEntity<MedicalTestRecord> getOne(@PathVariable Long patientId, @PathVariable Long id) {
		Optional<MedicalTestRecord> dto = testService.getOneRecord(patientId, id);
		return dto.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
	}

	@PostMapping
	public ResponseEntity<MedicalTestRecord> create(@PathVariable Long patientId,
			@RequestBody MedicalTestRecord payload) {
		MedicalTestRecord created = testService.createForPatientRecord(patientId, payload);
		URI location = URI.create(String.format("/api/patients/%d/tests/%d", patientId, created.getId()));
		return ResponseEntity.created(location).body(created);
	}

	public ResponseEntity<MedicalTestRecord> update(@PathVariable Long patientId, @PathVariable Long id,
			@RequestBody MedicalTestRecord payload) {
		Optional<MedicalTestRecord> updated = testService.updateForPatientRecord(patientId, id, payload);
		return updated.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable Long patientId, @PathVariable Long id) {
		if (testService.deleteForPatientRecord(patientId, id)) {
			return ResponseEntity.noContent().build();
		}
		return ResponseEntity.notFound().build();
	}
}
