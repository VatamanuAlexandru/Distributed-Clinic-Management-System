package com.clinic.appointment.service;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import com.clinic.appointment.entity.Appointment;
import com.clinic.appointment.entity.AppointmentStatus;
import com.clinic.appointment.repository.AppointmentRepository;
import com.clinic.common.service.BaseService;
import com.clinic.doctor.entity.Doctor;
import com.clinic.doctor.repository.DoctorRepository;
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

	@Override
	protected JpaRepository<Appointment, Long> getRepository() {
		return appointmentRepository;
	}

	public Appointment makeAppointment(Long doctorId, Long patientId, String reason, LocalDateTime appointmentDate) {
		Optional<Patient> patientOpt = patientRepository.findById(patientId);
		Optional<Doctor> doctorOpt = doctorRepository.findById(doctorId);

		if (!patientOpt.isPresent()) {
			throw new IllegalStateException("Patient not found with ID: " + patientId);
		}
		if (!doctorOpt.isPresent()) {
			throw new IllegalStateException("Doctor not found with ID: " + doctorId);
		}

		Doctor doctor = doctorOpt.get();
		Patient patient = patientOpt.get();

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
		appointment.setAppointmentDate(appointmentDate);
		appointment.setStatus(AppointmentStatus.PENDING);
		appointment.setOffice(doctor.getOffice());
		appointment.setReason(reason);
		return appointmentRepository.save(appointment);

	}

}
