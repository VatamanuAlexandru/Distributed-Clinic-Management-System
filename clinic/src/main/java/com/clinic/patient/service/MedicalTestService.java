package com.clinic.patient.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import com.clinic.common.service.BaseService;
import com.clinic.mapper.ModelMapper;
import com.clinic.patient.dto.MedicalTestRecord;
import com.clinic.patient.entity.MedicalHistory;
import com.clinic.patient.entity.MedicalTest;
import com.clinic.patient.entity.Patient;
import com.clinic.patient.entity.TestCategory;
import com.clinic.patient.entity.TestParameterResult;
import com.clinic.patient.repository.MedicalTestRepository;
import com.clinic.patient.repository.PatientRepository;

@Service
public class MedicalTestService extends BaseService<MedicalTest> {

	@Autowired
	private MedicalTestRepository medicalTestRepository;

	@Autowired
	private MedicalHistoryService historyService;

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private PatientRepository patientRepository;

	@Override
	protected JpaRepository<MedicalTest, Long> getRepository() {
		return medicalTestRepository;
	}

	public List<MedicalTest> listByPatient(Long patientId) {
		return medicalTestRepository.findByMedicalHistory_Patient_Id(patientId);
	}

	public Optional<MedicalTest> getOneEntity(Long patientId, Long testId) {
		return findById(testId).filter(t -> t.getMedicalHistory().getPatient().getId().equals(patientId));
	}

	public MedicalTest createEntity(Long patientId, MedicalTestRecord record) {
		MedicalHistory hist = null;

		if (record.getMedicalHistory() != null && record.getMedicalHistory().getId() != null) {
			hist = historyService.findById(record.getMedicalHistory().getId())
					.filter(h -> h.getPatient().getId().equals(patientId)).orElse(null);
		}

		if (hist == null) {
			hist = new MedicalHistory();
			Patient patient = patientRepository.findById(patientId)
					.orElseThrow(() -> new IllegalArgumentException("Pacient inexistent: " + patientId));
			hist.setPatient(patient);
			String historyName = (record.getTestName() != null && !record.getTestName().isBlank())
					? "Istoric pentru: " + record.getTestName()
					: "Istoric medical auto";
			hist.setName(historyName);
			hist.setDate(LocalDateTime.now());
			hist = historyService.save(hist);
		}

		MedicalTest test = new MedicalTest();
		test.setMedicalHistory(hist);
		test.setTestName(record.getTestName() != null ? record.getTestName() : "Test fără nume");
		test.setCategory(record.getCategory() != null ? record.getCategory() : TestCategory.OTHER);
		test.setResult(record.getResult() != null ? record.getResult() : "");
		test.setTestDate(record.getTestDate() != null ? record.getTestDate() : LocalDateTime.now());

		if (record.getParameterResults() != null) {
			List<TestParameterResult> paramEntities = record.getParameterResults().stream().map(paramRecord -> {
				TestParameterResult param = new TestParameterResult();
				param.setMedicalTest(test);
				param.setParameterName(paramRecord.getParameterName());
				param.setValue(paramRecord.getValue());
				param.setUnit(paramRecord.getUnit());
				param.setReferenceRange(paramRecord.getReferenceRange());
				param.setNotes(paramRecord.getNotes());
				return param;
			}).toList();
			test.setParameterResults(paramEntities);
		} else {
			test.setParameterResults(List.of());
		}

		return save(test);
	}

	public Optional<MedicalTest> updateEntity(Long patientId, Long testId, MedicalTestRecord record) {
		return getOneEntity(patientId, testId).map(existing -> {
			existing.setTestName(record.getTestName());
			existing.setCategory(record.getCategory());
			existing.setResult(record.getResult());
			existing.setTestDate(record.getTestDate());
			return save(existing);
		});
	}

	public boolean deleteEntity(Long patientId, Long testId) {
		return getOneEntity(patientId, testId).map(t -> {
			delete(t.getId());
			return true;
		}).orElse(false);
	}

	public List<MedicalTestRecord> listByPatientRecord(Long patientId) {
		return listByPatient(patientId).stream().map(e -> modelMapper.convertToDto(e, MedicalTestRecord.class))
				.collect(Collectors.toList());
	}

	public Optional<MedicalTestRecord> getOneRecord(Long patientId, Long testId) {
		return getOneEntity(patientId, testId).map(e -> modelMapper.convertToDto(e, MedicalTestRecord.class));
	}

	public MedicalTestRecord createForPatientRecord(Long patientId, MedicalTestRecord record) {
		MedicalTest created = createEntity(patientId, record);
		return modelMapper.convertToDto(created, MedicalTestRecord.class);
	}

	public Optional<MedicalTestRecord> updateForPatientRecord(Long patientId, Long testId, MedicalTestRecord record) {
		return updateEntity(patientId, testId, record).map(e -> modelMapper.convertToDto(e, MedicalTestRecord.class));
	}

	public boolean deleteForPatientRecord(Long patientId, Long testId) {
		return deleteEntity(patientId, testId);
	}
}
