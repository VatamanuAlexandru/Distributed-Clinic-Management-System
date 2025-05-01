package com.clinic.patient.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import com.clinic.common.service.BaseService;
import com.clinic.patient.entity.Patient;
import com.clinic.patient.repository.PatientRepository;

@Service
public class PatientService extends BaseService<Patient> {

	@Autowired
	private PatientRepository patientRepository;

	@Override
	protected JpaRepository<Patient, Long> getRepository() {
		return patientRepository;
	}

	public void createPatient(Long personId) {
		Patient patient = new Patient();
		patient.setPersonId(personId);
		save(patient);
	}

}
