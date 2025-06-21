package com.clinic.patient.service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import com.clinic.common.service.BaseService;
import com.clinic.patient.dto.DiagnosisRecord;
import com.clinic.patient.dto.MedicalHistoryEntryRecord;
import com.clinic.patient.dto.MedicalTestRecord;
import com.clinic.patient.entity.Diagnosis;
import com.clinic.patient.entity.MedicalHistory;
import com.clinic.patient.entity.MedicalTest;
import com.clinic.patient.entity.Patient;
import com.clinic.patient.repository.MedicalHistoryRepository;
import com.clinic.patient.repository.PatientRepository;

@Service
public class MedicalHistoryService extends BaseService<MedicalHistory> {

	@Autowired
	private MedicalHistoryRepository medicalHistoryRepository;

	@Autowired
	private PatientRepository patientRepository;

	@Override
	protected JpaRepository<MedicalHistory, Long> getRepository() {
		return medicalHistoryRepository;
	}

	public List<MedicalHistoryEntryRecord> getMedicalHistoryEntriesByPatientId(Long patientId) {
		Patient patient = patientRepository.findById(patientId)
				.orElseThrow(() -> new RuntimeException("Pacientul nu a fost găsit!"));

		List<MedicalHistoryEntryRecord> entries = new ArrayList<>();

		for (MedicalHistory history : patient.getMedicalHistories()) {
			for (Diagnosis diagnosis : history.getDiagnosis()) {
				DiagnosisRecord diagnosisDto = new DiagnosisRecord();
				diagnosisDto.setId(diagnosis.getId());
				diagnosisDto.setDiagnosisDate(diagnosis.getDiagnosisDate());

				if (diagnosis.getMedicalHistory() != null) {
					var histDto = new com.clinic.patient.dto.MedicalHistoryRecord();
					histDto.setId(history.getId());
					histDto.setName(history.getName());
					diagnosisDto.setMedicalHistory(histDto);
				}
				if (diagnosis.getCondition() != null) {
					var condDto = new com.clinic.patient.dto.MedicalConditionRecord();
					condDto.setId(diagnosis.getCondition().getId());
					condDto.setName(diagnosis.getCondition().getName());
					diagnosisDto.setCondition(condDto);
				}

				MedicalHistoryEntryRecord record = new MedicalHistoryEntryRecord();
				record.setType("DIAGNOSIS");
				record.setDate(diagnosis.getDiagnosisDate());
				record.setData(diagnosisDto);

				entries.add(record);
			}

			for (MedicalTest test : history.getMedicalTests()) {
				MedicalTestRecord testDto = new MedicalTestRecord();
				testDto.setId(test.getId());
				testDto.setTestName(test.getTestName());
				testDto.setCategory(test.getCategory());
				testDto.setResult(test.getResult());
				testDto.setTestDate(test.getTestDate());

				MedicalHistoryEntryRecord record = new MedicalHistoryEntryRecord();
				record.setType("TEST");
				record.setDate(test.getTestDate());
				record.setData(testDto);

				entries.add(record);
			}
		}

		entries.sort(Comparator.comparing(MedicalHistoryEntryRecord::getDate).reversed());

		return entries;
	}
}
