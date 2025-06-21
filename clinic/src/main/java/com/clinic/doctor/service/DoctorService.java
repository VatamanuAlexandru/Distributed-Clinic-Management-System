package com.clinic.doctor.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import com.clinic.appointment.entity.Appointment;
import com.clinic.appointment.entity.AppointmentStatus;
import com.clinic.appointment.repository.AppointmentRepository;
import com.clinic.client.PersonClient;
import com.clinic.common.service.BaseService;
import com.clinic.doctor.dto.DoctorRecord;
import com.clinic.doctor.entity.Doctor;
import com.clinic.doctor.repository.DoctorRepository;
import com.clinic.mapper.ModelMapper;
import com.clinic.patient.dto.PatientRecord;

@Service
public class DoctorService extends BaseService<Doctor> {

	@Autowired
	private DoctorRepository doctorRepository;

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private PersonClient personClient;
	
	@Autowired
	private AppointmentRepository appointmentRepository;

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

	public DoctorRecord getDoctorById(Long id) {
		Doctor doctor = doctorRepository.findById(id).orElse(null);
		DoctorRecord doctorRecord = new DoctorRecord();
		if (doctor != null) {
			doctorRecord = modelMapper.convertToDto(doctor, DoctorRecord.class);
			try {
				var person = personClient.getPersonById(doctor.getPersonId());
				doctorRecord.setPerson(person);
			} catch (Exception e) {
				System.err.println("Persoana cu ID-ul " + doctor.getPersonId() + " nu a fost găsită!");
			}
		}

		return doctorRecord;
	}

	public List<DoctorRecord> getDoctorByServiceId(Long serviceId) {
		List<Doctor> doctors = doctorRepository.findByServiceId(serviceId);
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
	
	public List<PatientRecord> getCompletedPatientsByDoctor(Long doctorId) {
		List<Appointment> appts = appointmentRepository.findByDoctorIdAndStatus(doctorId, AppointmentStatus.COMPLETED);

		return appts.stream().map(Appointment::getPatient).distinct().map(patientEntity -> {
			PatientRecord pr = modelMapper.convertToDto(patientEntity, PatientRecord.class);
			var personDto = personClient.getPersonById(patientEntity.getPersonId());
			pr.setPerson(personDto);
			return pr;
		}).collect(Collectors.toList());
	}



}
