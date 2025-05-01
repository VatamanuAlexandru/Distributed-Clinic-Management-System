package com.clinic.doctor.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import com.clinic.client.PersonClient;
import com.clinic.common.service.BaseService;
import com.clinic.doctor.dto.DoctorRecord;
import com.clinic.doctor.entity.Doctor;
import com.clinic.doctor.repository.DoctorRepository;
import com.clinic.mapper.ModelMapper;

@Service
public class DoctorService extends BaseService<Doctor> {

	@Autowired
	private DoctorRepository doctorRepository;

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private PersonClient personClient;

	@Override
	protected JpaRepository<Doctor, Long> getRepository() {
		return doctorRepository;
	}

	public void createDoctor(Long personId) {
		Doctor doctor = new Doctor();
		doctor.setPersonId(personId);
		save(doctor);
	}

	public List<DoctorRecord> getDoctorsByDepartmentId(Long departmentId) {
		List<Doctor> doctors = doctorRepository.findByDepartmentId(departmentId);
		List<DoctorRecord> doctorRecords = new ArrayList<>();

		if (!doctors.isEmpty()) {
			doctorRecords = doctors.stream().map(d -> modelMapper.convertToDto(d, DoctorRecord.class))
					.collect(Collectors.toList());
		}

		for (int i = 0; i < doctorRecords.size(); i++) {
			DoctorRecord record = doctorRecords.get(i);
			Long personId = doctors.get(i).getPersonId();

			try {
				var person = personClient.getPersonById(personId);
				record.setPerson(person);
			} catch (Exception e) {
				System.err.println("Persoana cu ID-ul " + personId + " nu a fost găsită!");
			}
		}

		return doctorRecords;
	}

}
