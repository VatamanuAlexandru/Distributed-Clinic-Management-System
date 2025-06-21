package com.clinic.appointment.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import com.clinic.appointment.entity.Appointment;
import com.clinic.appointment.entity.AppointmentStatus;
import com.clinic.doctor.entity.Doctor;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

	boolean existsByDoctorAndAppointmentDate(Doctor doctor, LocalDateTime appointmentDate);

	List<Appointment> findByDoctorAndAppointmentDateBetween(Doctor doctor, LocalDateTime start, LocalDateTime end);

	@EntityGraph(attributePaths = { "doctor", "doctor.medicalTitle", "doctor.office" })
	List<Appointment> findByPatientId(Long patientId);
	
	List<Appointment> findByDoctorId(Long doctorId);
	
	List<Appointment> findByDoctorIdAndStatus(Long doctorId, AppointmentStatus status);
}

