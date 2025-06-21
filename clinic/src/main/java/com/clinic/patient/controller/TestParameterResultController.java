package com.clinic.patient.controller;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.clinic.patient.dto.TestParameterResultRecord;
import com.clinic.patient.repository.TestParameterResultService;

@RestController
@RequestMapping("/patients/{patientId}/tests/{testId}/parameters")
public class TestParameterResultController {

	@Autowired
	private TestParameterResultService service;

	@GetMapping
	public List<TestParameterResultRecord> list(@PathVariable Long patientId, @PathVariable Long testId) {
		return service.listByTest(patientId, testId);
	}

	@GetMapping("/{paramId}")
	public ResponseEntity<TestParameterResultRecord> getOne(@PathVariable Long patientId, @PathVariable Long testId,
			@PathVariable Long paramId) {
		Optional<TestParameterResultRecord> dto = service.getOne(patientId, testId, paramId);
		return dto.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
	}

	@PostMapping
	public ResponseEntity<TestParameterResultRecord> create(@PathVariable Long patientId, @PathVariable Long testId,
			@RequestBody TestParameterResultRecord payload) {
		TestParameterResultRecord created = service.create(patientId, testId, payload);
		URI location = URI
				.create(String.format("/patients/%d/tests/%d/parameters/%d", patientId, testId, created.getId()));
		return ResponseEntity.created(location).body(created);
	}

	@PutMapping("/{paramId}")
	public ResponseEntity<TestParameterResultRecord> update(@PathVariable Long patientId, @PathVariable Long testId,
			@PathVariable Long paramId, @RequestBody TestParameterResultRecord payload) {
		Optional<TestParameterResultRecord> updated = service.update(patientId, testId, paramId, payload);
		return updated.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
	}

	@DeleteMapping("/{paramId}")
	public ResponseEntity<Void> delete(@PathVariable Long patientId, @PathVariable Long testId,
			@PathVariable Long paramId) {
		if (service.delete(patientId, testId, paramId)) {
			return ResponseEntity.noContent().build();
		}
		return ResponseEntity.notFound().build();
	}
}
