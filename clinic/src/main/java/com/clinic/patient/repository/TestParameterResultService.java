package com.clinic.patient.repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import com.clinic.common.service.BaseService;
import com.clinic.mapper.ModelMapper;
import com.clinic.patient.dto.TestParameterResultRecord;
import com.clinic.patient.entity.MedicalTest;
import com.clinic.patient.entity.TestParameterResult;
import com.clinic.patient.service.MedicalTestService;

@Service
public class TestParameterResultService extends BaseService<TestParameterResult> {

	@Autowired
	private TestParameterResultRepository testParameterResultRepository;

	@Autowired
	private MedicalTestService medicalTestService;

	@Autowired
	private ModelMapper mapper;

	@Override
	protected JpaRepository<TestParameterResult, Long> getRepository() {
		return testParameterResultRepository;
	}

	public List<TestParameterResultRecord> listByTest(Long patientId, Long testId) {
		MedicalTest test = medicalTestService.getOneEntity(patientId, testId)
				.orElseThrow(() -> new IllegalArgumentException("Invalid test or patient"));
		return testParameterResultRepository.findByMedicalTestId(testId).stream()
				.map(e -> mapper.convertToDto(e, TestParameterResultRecord.class)).collect(Collectors.toList());
	}

	public Optional<TestParameterResultRecord> getOne(Long patientId, Long testId, Long paramId) {
		return testParameterResultRepository.findById(paramId)
				.filter(p -> p.getMedicalTest().getId().equals(testId)
						&& p.getMedicalTest().getMedicalHistory().getPatient().getId().equals(patientId))
				.map(e -> mapper.convertToDto(e, TestParameterResultRecord.class));
	}

	public TestParameterResultRecord create(Long patientId, Long testId, TestParameterResultRecord record) {
		MedicalTest test = medicalTestService.getOneEntity(patientId, testId)
				.orElseThrow(() -> new IllegalArgumentException("Invalid test or patient"));

		TestParameterResult entity = new TestParameterResult();
		entity.setParameterName(record.getParameterName());
		entity.setValue(record.getValue());
		entity.setUnit(record.getUnit());
		entity.setReferenceRange(record.getReferenceRange());
		entity.setNotes(record.getNotes());
		entity.setMedicalTest(test);

		TestParameterResult saved = save(entity);

		return mapper.convertToDto(saved, TestParameterResultRecord.class);
	}

	public Optional<TestParameterResultRecord> update(Long patientId, Long testId, Long paramId,
			TestParameterResultRecord record) {
		return testParameterResultRepository.findById(paramId)
				.filter(p -> p.getMedicalTest().getId().equals(testId)
						&& p.getMedicalTest().getMedicalHistory().getPatient().getId().equals(patientId))
				.map(existing -> {
					existing.setParameterName(record.getParameterName());
					existing.setValue(record.getValue());
					existing.setUnit(record.getUnit());
					existing.setReferenceRange(record.getReferenceRange());
					existing.setNotes(record.getNotes());
					return save(existing);
				}).map(e -> mapper.convertToDto(e, TestParameterResultRecord.class));
	}

	public boolean delete(Long patientId, Long testId, Long paramId) {
		return testParameterResultRepository.findById(paramId).filter(p -> p.getMedicalTest().getId().equals(testId)
				&& p.getMedicalTest().getMedicalHistory().getPatient().getId().equals(patientId)).map(p -> {
					delete(p.getId());
					return true;
				}).orElse(false);
	}
}