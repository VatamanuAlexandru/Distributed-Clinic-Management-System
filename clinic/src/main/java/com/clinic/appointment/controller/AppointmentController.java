package com.clinic.appointment.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.clinic.appointment.dto.AppointmentRecord;
import com.clinic.appointment.dto.AppointmentRequest;
import com.clinic.appointment.service.AppointmentService;

@RestController
@RequestMapping("/appointment")
public class AppointmentController {

	@Autowired
	private AppointmentService appointmentService;

	@PostMapping
	public ResponseEntity<?> createAppointment(@RequestBody AppointmentRequest appointmentRequest) {
		AppointmentRecord appointmentRecord = appointmentService.makeAppointment(appointmentRequest.getDoctorId(),
				appointmentRequest.getPatientId(), appointmentRequest.getReason(),
				appointmentRequest.getAppointmentDate());
		return ResponseEntity.ok(appointmentRecord);

	}
}
