package com.clinic.patient.service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.clinic.patient.dto.DiagnosisRecord;
import com.clinic.patient.dto.MedicalConditionRecord;
import com.clinic.patient.dto.MedicalHistoryRecord;
import com.clinic.patient.entity.Diagnosis;
import com.clinic.patient.entity.MedicalCondition;
import com.clinic.patient.entity.MedicalHistory;
import com.clinic.patient.entity.Patient;
import com.clinic.patient.repository.DiagnosisRepository;
import com.clinic.patient.repository.MedicalConditionRepository;
import com.clinic.patient.repository.MedicalHistoryRepository;
import com.clinic.patient.repository.PatientRepository;

@Service
public class DiagnosisService {

	@Autowired
	private MedicalHistoryRepository medicalHistoryRepository;
	@Autowired
	private DiagnosisRepository diagnosisRepository;
	@Autowired
	private MedicalConditionRepository medicalConditionRepository;
	@Autowired
	private PatientRepository patientRepository;

	// DTO -> Entity helper
	private DiagnosisRecord toDiagnosisRecord(Diagnosis diagnosis) {
		DiagnosisRecord dto = new DiagnosisRecord();
		dto.setId(diagnosis.getId());
		dto.setDiagnosisDate(diagnosis.getDiagnosisDate());

		MedicalHistory history = diagnosis.getMedicalHistory();
		if (history != null) {
			MedicalHistoryRecord histDto = new MedicalHistoryRecord();
			histDto.setId(history.getId());
			histDto.setName(history.getName());
			dto.setMedicalHistory(histDto);
		}

		MedicalCondition cond = diagnosis.getCondition();
		if (cond != null) {
			MedicalConditionRecord condDto = new MedicalConditionRecord();
			condDto.setId(cond.getId());
			condDto.setName(cond.getName());
			dto.setCondition(condDto);
		}

		return dto;
	}

	private void updateDiagnosisFromDto(Diagnosis diagnosis, DiagnosisRecord dto) {
		diagnosis.setDiagnosisDate(dto.getDiagnosisDate());
	}

	public List<DiagnosisRecord> getDiagnosesForPatient(Long patientId) {
		List<MedicalHistory> histories = medicalHistoryRepository.findByPatientId(patientId);
		List<DiagnosisRecord> result = new ArrayList<>();
		for (MedicalHistory history : histories) {
			for (Diagnosis diagnosis : history.getDiagnosis()) {
				result.add(toDiagnosisRecord(diagnosis));
			}
		}
		result.sort(Comparator.comparing(DiagnosisRecord::getDiagnosisDate).reversed());
		return result;
	}

	public DiagnosisRecord createDiagnosis(Long patientId, DiagnosisRecord dto) {
		MedicalHistory history = null;
		if (dto.getMedicalHistory() != null && dto.getMedicalHistory().getId() != null) {
			history = medicalHistoryRepository.findById(dto.getMedicalHistory().getId())
					.filter(h -> h.getPatient().getId().equals(patientId))
					.orElseThrow(() -> new RuntimeException("Istoric inexistent pt. acest pacient!"));
		} else {
			Patient patient = patientRepository.findById(patientId)
					.orElseThrow(() -> new RuntimeException("Pacient inexistent!"));
			history = new MedicalHistory();
			history.setPatient(patient);
			history.setDate(dto.getDiagnosisDate() != null ? dto.getDiagnosisDate() : java.time.LocalDateTime.now());
			history.setName("Episod " + history.getDate().toLocalDate());
			history = medicalHistoryRepository.save(history);
		}

		MedicalCondition condition = null;
		if (dto.getCondition() != null) {
			if (dto.getCondition().getId() != null) {
				condition = medicalConditionRepository.findById(dto.getCondition().getId())
						.orElseThrow(() -> new RuntimeException("Condiție medicală inexistentă!"));
			} else if (dto.getCondition().getName() != null && !dto.getCondition().getName().isBlank()) {
				condition = medicalConditionRepository.findByName(dto.getCondition().getName()).orElseGet(() -> {
					MedicalCondition newCond = new MedicalCondition();
					newCond.setName(dto.getCondition().getName());
					return medicalConditionRepository.save(newCond);
				});
			}
		}

		Diagnosis diagnosis = new Diagnosis();
		diagnosis.setMedicalHistory(history);
		diagnosis.setDiagnosisDate(dto.getDiagnosisDate());
		diagnosis.setCondition(condition);

		Diagnosis saved = diagnosisRepository.save(diagnosis);
		return toDiagnosisRecord(saved);
	}

	public DiagnosisRecord updateDiagnosis(Long patientId, Long diagnosisId, DiagnosisRecord dto) {
		Diagnosis diagnosis = diagnosisRepository.findById(diagnosisId)
				.filter(d -> d.getMedicalHistory().getPatient().getId().equals(patientId))
				.orElseThrow(() -> new RuntimeException("Diagnostic inexistent!"));

		updateDiagnosisFromDto(diagnosis, dto);
		Diagnosis updated = diagnosisRepository.save(diagnosis);
		return toDiagnosisRecord(updated);
	}

	public void deleteDiagnosis(Long patientId, Long diagnosisId) {
		Diagnosis diagnosis = diagnosisRepository.findById(diagnosisId)
				.filter(d -> d.getMedicalHistory().getPatient().getId().equals(patientId))
				.orElseThrow(() -> new RuntimeException("Diagnostic inexistent!"));
		diagnosisRepository.delete(diagnosis);
	}
}
