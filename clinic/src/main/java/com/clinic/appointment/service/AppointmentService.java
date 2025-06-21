package com.clinic.appointment.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import com.clinic.appointment.dto.AppointmentRecord;
import com.clinic.appointment.entity.Appointment;
import com.clinic.appointment.entity.AppointmentStatus;
import com.clinic.appointment.repository.AppointmentRepository;
import com.clinic.client.PaymentClient;
import com.clinic.client.PersonClient;
import com.clinic.common.dto.CreatePaymentObligationRequest;
import com.clinic.common.service.BaseService;
import com.clinic.doctor.dto.DoctorRecord;
import com.clinic.doctor.entity.Doctor;
import com.clinic.doctor.entity.MedicalService;
import com.clinic.doctor.repository.DoctorRepository;
import com.clinic.doctor.repository.MedicalServiceRepository;
import com.clinic.mapper.ModelMapper;
import com.clinic.patient.dto.PatientRecord;
import com.clinic.patient.entity.Patient;
import com.clinic.patient.repository.PatientRepository;

@Service
public class AppointmentService extends BaseService<Appointment> {

	@Autowired
	private AppointmentRepository appointmentRepository;

	@Autowired
	private PatientRepository patientRepository;

	@Autowired
	private DoctorRepository doctorRepository;

	@Autowired
	private MedicalServiceRepository medicalServiceRepository;

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private PersonClient personClient;

	@Autowired
	private PaymentClient paymentClient;

	@Override
	protected JpaRepository<Appointment, Long> getRepository() {
		return appointmentRepository;
	}

	public void makeAppointment(Long doctorId, Long patientId, String reason, LocalDateTime appointmentDate,
			Long serviceId) {
		Optional<Patient> patientOpt = patientRepository.findById(patientId);
		Optional<Doctor> doctorOpt = doctorRepository.findById(doctorId);
		Optional<MedicalService> medicalServiceOpt = medicalServiceRepository.findById(serviceId);

		if (!patientOpt.isPresent()) {
			throw new IllegalStateException("Patient not found with ID: " + patientId);
		}
		if (!doctorOpt.isPresent()) {
			throw new IllegalStateException("Doctor not found with ID: " + doctorId);
		}

		if (!medicalServiceOpt.isPresent()) {
			throw new IllegalStateException("Medical Service not found with  ID: " + serviceId);
		}

		Doctor doctor = doctorOpt.get();
		Patient patient = patientOpt.get();
		MedicalService medicalService = medicalServiceOpt.get();

		if (doctor.getOffice() == null) {
			throw new IllegalStateException("Doctor has no assigned office.");
		}

		boolean hasAnAppointmentAtThatDate = appointmentRepository.existsByDoctorAndAppointmentDate(doctor,
				appointmentDate);

		if (hasAnAppointmentAtThatDate) {
			throw new IllegalStateException("Exista deja");
		}

		Appointment appointment = new Appointment();

		appointment.setDoctor(doctor);
		appointment.setPatient(patient);
		appointment.setAppointmentDate(appointmentDate.withSecond(0).withNano(0));
		appointment.setStatus(AppointmentStatus.PENDING);
		appointment.setOffice(doctor.getOffice());
		appointment.setReason(reason);
		appointment.setService(medicalService);
		appointmentRepository.save(appointment);
	}

	public List<AppointmentRecord> getAppointmentsByPatientId(Long patientId) {
		List<Appointment> appointments = appointmentRepository.findByPatientId(patientId);
		List<AppointmentRecord> appointmentRecords = appointments.stream()
				.map(a -> modelMapper.convertToDto(a, AppointmentRecord.class)).collect(Collectors.toList());

		Map<Long, DoctorRecord> uniqueDoctorRecords = new HashMap<>();
		for (AppointmentRecord ar : appointmentRecords) {
			DoctorRecord doctor = ar.getDoctor();
			if (doctor != null && !uniqueDoctorRecords.containsKey(doctor.getId())) {
				uniqueDoctorRecords.put(doctor.getId(), doctor);
			}
		}

		List<Doctor> doctors = doctorRepository.findByIds(new ArrayList<>(uniqueDoctorRecords.keySet()));

		for (Doctor doctor : doctors) {
			Long id = doctor.getId();
			Long personId = doctor.getPersonId();

			try {
				var person = personClient.getPersonById(personId);
				DoctorRecord record = uniqueDoctorRecords.get(id);
				if (record != null) {
					record.setPerson(person);
				}
			} catch (Exception e) {
				System.err.println("Persoana cu ID-ul " + personId + " nu a fost găsită!");
			}
		}

		for (AppointmentRecord ar : appointmentRecords) {
			Long doctorId = ar.getDoctor().getId();
			ar.setDoctor(uniqueDoctorRecords.get(doctorId));
		}

		return appointmentRecords;
	}

	public List<AppointmentRecord> getAppointmentsByDoctorId(Long doctorId) {
		List<Appointment> appointments = appointmentRepository.findByDoctorId(doctorId);
		List<AppointmentRecord> appointmentRecords = appointments.stream()
				.map(a -> modelMapper.convertToDto(a, AppointmentRecord.class)).collect(Collectors.toList());

		for (AppointmentRecord ar : appointmentRecords) {
			PatientRecord patient = ar.getPatient();
			if (patient != null && patient.getPerson() == null) {
				Long patientId = patient.getId();
				Patient persistentPatient = patientRepository.findById(patientId).orElse(null);

				if (persistentPatient != null) {
					try {
						var person = personClient.getPersonById(persistentPatient.getPersonId());
						patient.setPerson(person);
					} catch (Exception e) {
						System.err
								.println("Persoană cu ID-ul " + persistentPatient.getPersonId() + " nu a fost găsită.");
					}
				}
			}
		}

		return appointmentRecords;
	}

	public void updateStatus(Long appointmentId, AppointmentStatus status) {
		var appointmentOpt = appointmentRepository.findById(appointmentId);

		if (appointmentOpt.isEmpty()) {
			throw new RuntimeException("Programarea nu a fost găsită");
		}

		var appointment = appointmentOpt.get();
		appointment.setStatus(status);
		appointmentRepository.save(appointment);

		if (status == AppointmentStatus.CONFIRMED) {
			CreatePaymentObligationRequest request = new CreatePaymentObligationRequest();
			request.setPatientId(appointment.getPatient().getId());
			request.setAmount(BigDecimal.valueOf(appointment.getService().getPrice()));
			request.setDescription("Plată pentru " + appointment.getService().getName());
			request.setDueDate(appointment.getAppointmentDate());

			paymentClient.createPaymentObligation(request);
		}
	}

}
