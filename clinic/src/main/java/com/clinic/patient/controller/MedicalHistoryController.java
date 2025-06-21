package com.clinic.patient.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.clinic.patient.dto.MedicalHistoryEntryRecord;
import com.clinic.patient.service.MedicalHistoryService;

@RestController
@RequestMapping("/medical-history")
public class MedicalHistoryController {

	@Autowired
	private MedicalHistoryService medicalHistoryService;

	@GetMapping("/{patientId}")
	public ResponseEntity<List<MedicalHistoryEntryRecord>> getHistory(@PathVariable Long patientId) {
		return ResponseEntity.ok(medicalHistoryService.getMedicalHistoryEntriesByPatientId(patientId));
	}

}
