package com.clinic.appointment.repository;

import java.time.LocalDateTime;

import org.springframework.data.jpa.repository.JpaRepository;

import com.clinic.appointment.entity.Appointment;
import com.clinic.doctor.entity.Doctor;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

	boolean existsByDoctorAndAppointmentDate(Doctor doctor, LocalDateTime appointmentDate);
}
