package com.clinic.patient.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import com.clinic.common.service.BaseService;
import com.clinic.patient.dto.DiagnosisRecord;
import com.clinic.patient.dto.MedicationRecord;
import com.clinic.patient.dto.TreatmentRecord;
import com.clinic.patient.entity.Diagnosis;
import com.clinic.patient.entity.Medication;
import com.clinic.patient.entity.Patient;
import com.clinic.patient.entity.Treatment;
import com.clinic.patient.repository.DiagnosisRepository;
import com.clinic.patient.repository.PatientRepository;
import com.clinic.patient.repository.TreatmentRepository;

@Service
public class TreatmentService extends BaseService<Treatment> {

	@Autowired
	private TreatmentRepository treatmentRepository;

	@Autowired
	private PatientRepository patientRepository;

	@Autowired
	private DiagnosisRepository diagnosisRepository;

	@Override
	protected JpaRepository<Treatment, Long> getRepository() {
		return treatmentRepository;
	}

	private TreatmentRecord toTreatmentRecord(Treatment treatment) {
		TreatmentRecord dto = new TreatmentRecord();
		dto.setId(treatment.getId());
		dto.setTreatmentType(treatment.getTreatmentType());
		dto.setDetails(treatment.getDetails());
		dto.setDuration(treatment.getDuration());

		if (treatment.getDiagnosis() != null) {
			Diagnosis d = treatment.getDiagnosis();
			DiagnosisRecord diagDto = new DiagnosisRecord();
			diagDto.setId(d.getId());
			diagDto.setDiagnosisDate(d.getDiagnosisDate());
			dto.setDiagnosis(diagDto);
		}

		if (treatment.getMedications() != null) {
			List<MedicationRecord> meds = new ArrayList<>();
			for (Medication m : treatment.getMedications()) {
				MedicationRecord mDto = new MedicationRecord();
				mDto.setId(m.getId());
				mDto.setName(m.getName());
				mDto.setDosage(m.getDosage());
				mDto.setFrequency(m.getFrequency());
				mDto.setAdministrationRoute(m.getAdministrationRoute());
				mDto.setSideEffects(m.getSideEffects());
				meds.add(mDto);
			}
			dto.setMedications(meds);
		}
		return dto;
	}

	public List<TreatmentRecord> getTreatmentsForPatient(Long patientId) {
		Patient patient = patientRepository.findById(patientId)
				.orElseThrow(() -> new RuntimeException("Pacient inexistent!"));

		List<Diagnosis> diagnoses = patient.getMedicalHistories().stream().flatMap(h -> h.getDiagnosis().stream())
				.collect(Collectors.toList());

		return diagnoses.stream().map(Diagnosis::getTreatment).filter(Objects::nonNull).map(this::toTreatmentRecord)
				.collect(Collectors.toList());
	}

	public TreatmentRecord getTreatmentById(Long treatmentId) {
		Treatment treatment = treatmentRepository.findById(treatmentId)
				.orElseThrow(() -> new RuntimeException("Tratament inexistent!"));
		return toTreatmentRecord(treatment);
	}

	public TreatmentRecord createTreatment(Long diagnosisId, TreatmentRecord dto) {
		Diagnosis diagnosis = diagnosisRepository.findById(diagnosisId)
				.orElseThrow(() -> new RuntimeException("Diagnostic inexistent pentru tratament!"));

		Treatment treatment = new Treatment();
		treatment.setDiagnosis(diagnosis);
		treatment.setTreatmentType(dto.getTreatmentType());
		treatment.setDetails(dto.getDetails());
		treatment.setDuration(dto.getDuration());

		List<Medication> meds = new ArrayList<>();
		if (dto.getMedications() != null) {
			for (MedicationRecord medDto : dto.getMedications()) {
				Medication med = new Medication();
				med.setName(medDto.getName());
				med.setDosage(medDto.getDosage());
				med.setFrequency(medDto.getFrequency());
				med.setAdministrationRoute(medDto.getAdministrationRoute());
				med.setSideEffects(medDto.getSideEffects());
				med.setTreatment(treatment);
				meds.add(med);
			}
		}
		treatment.setMedications(meds);

		Treatment saved = treatmentRepository.save(treatment);
		return toTreatmentRecord(saved);
	}

	public TreatmentRecord updateTreatment(Long treatmentId, TreatmentRecord dto) {
		Treatment treatment = treatmentRepository.findById(treatmentId)
				.orElseThrow(() -> new RuntimeException("Tratament inexistent!"));

		treatment.setTreatmentType(dto.getTreatmentType());
		treatment.setDetails(dto.getDetails());
		treatment.setDuration(dto.getDuration());
		List<Medication> meds = new ArrayList<>();
		if (dto.getMedications() != null) {
			for (MedicationRecord medDto : dto.getMedications()) {
				Medication med = new Medication();
				med.setName(medDto.getName());
				med.setDosage(medDto.getDosage());
				med.setFrequency(medDto.getFrequency());
				med.setAdministrationRoute(medDto.getAdministrationRoute());
				med.setSideEffects(medDto.getSideEffects());
				med.setTreatment(treatment);
				meds.add(med);
			}
		}
		treatment.setMedications(meds);

		Treatment updated = treatmentRepository.save(treatment);
		return toTreatmentRecord(updated);
	}

	public void deleteTreatment(Long treatmentId) {
		Treatment treatment = treatmentRepository.findById(treatmentId)
				.orElseThrow(() -> new RuntimeException("Tratament inexistent!"));
		treatmentRepository.delete(treatment);
	}
}
