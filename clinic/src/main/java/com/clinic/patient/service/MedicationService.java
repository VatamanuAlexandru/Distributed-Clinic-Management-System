package com.clinic.patient.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import com.clinic.common.service.BaseService;
import com.clinic.patient.dto.MedicationRecord;
import com.clinic.patient.entity.Medication;
import com.clinic.patient.entity.Treatment;
import com.clinic.patient.repository.MedicationRepository;
import com.clinic.patient.repository.TreatmentRepository;

@Service
public class MedicationService extends BaseService<Medication> {

	@Autowired
	private MedicationRepository medicationRepository;

	@Autowired
	private TreatmentRepository treatmentRepository;

	@Override
	protected JpaRepository<Medication, Long> getRepository() {
		return medicationRepository;
	}

	private MedicationRecord toMedicationRecord(Medication med) {
		MedicationRecord dto = new MedicationRecord();
		dto.setId(med.getId());
		dto.setName(med.getName());
		dto.setDosage(med.getDosage());
		dto.setFrequency(med.getFrequency());
		dto.setAdministrationRoute(med.getAdministrationRoute());
		dto.setSideEffects(med.getSideEffects());
		return dto;
	}

	public List<MedicationRecord> getMedicationsByTreatment(Long treatmentId) {
		Treatment treatment = treatmentRepository.findById(treatmentId)
				.orElseThrow(() -> new RuntimeException("Tratament inexistent!"));
		List<MedicationRecord> result = new ArrayList<>();
		for (Medication med : treatment.getMedications()) {
			result.add(toMedicationRecord(med));
		}
		return result;
	}

	public MedicationRecord addMedication(Long treatmentId, MedicationRecord dto) {
		Treatment treatment = treatmentRepository.findById(treatmentId)
				.orElseThrow(() -> new RuntimeException("Tratament inexistent!"));
		Medication med = new Medication();
		med.setName(dto.getName());
		med.setDosage(dto.getDosage());
		med.setFrequency(dto.getFrequency());
		med.setAdministrationRoute(dto.getAdministrationRoute());
		med.setSideEffects(dto.getSideEffects());
		med.setTreatment(treatment);

		Medication saved = medicationRepository.save(med);
		return toMedicationRecord(saved);
	}

	public MedicationRecord updateMedication(Long medId, MedicationRecord dto) {
		Medication med = medicationRepository.findById(medId)
				.orElseThrow(() -> new RuntimeException("Medicament inexistent!"));
		med.setName(dto.getName());
		med.setDosage(dto.getDosage());
		med.setFrequency(dto.getFrequency());
		med.setAdministrationRoute(dto.getAdministrationRoute());
		med.setSideEffects(dto.getSideEffects());

		Medication updated = medicationRepository.save(med);
		return toMedicationRecord(updated);
	}

	public void deleteMedication(Long medId) {
		Medication med = medicationRepository.findById(medId)
				.orElseThrow(() -> new RuntimeException("Medicament inexistent!"));
		medicationRepository.delete(med);
	}
}
