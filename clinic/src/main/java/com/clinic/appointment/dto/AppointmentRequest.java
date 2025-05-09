package com.clinic.appointment.dto;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class AppointmentRequest {

	private Long doctorId;
	private Long patientId;
	private Long serviceId;
	private String reason;
	private LocalDateTime appointmentDate;
}
