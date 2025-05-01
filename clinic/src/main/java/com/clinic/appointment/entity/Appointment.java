package com.clinic.appointment.entity;

import java.time.LocalDateTime;

import com.clinic.administrative.entity.Office;
import com.clinic.common.PersistableEntity;
import com.clinic.doctor.entity.Doctor;
import com.clinic.patient.entity.Patient;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "APPOINTMENT")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Appointment extends PersistableEntity {
	private static final long serialVersionUID = 1L;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "PATIENT_ID")
	private Patient patient;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "DOCTOR_ID")
	private Doctor doctor;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "OFFICE_ID")
	private Office office;

	@Column(name = "APPOINTMENT_DATE")
	private LocalDateTime appointmentDate;

	@Column(name = "STATUS")
	@Enumerated(EnumType.STRING)
	private AppointmentStatus status;

	@Column(name = "REASON")
	private String reason;

	@Column(name = "CREATED_AT")
	private LocalDateTime createdAt = LocalDateTime.now();

}
