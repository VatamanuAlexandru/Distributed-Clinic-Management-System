package com.clinic.appointment.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.clinic.appointment.dto.AppointmentRecord;
import com.clinic.appointment.dto.AppointmentRequest;
import com.clinic.appointment.dto.StatusUpdateRequest;
import com.clinic.appointment.entity.AppointmentStatus;
import com.clinic.appointment.service.AppointmentService;

@RestController
@RequestMapping("/appointment")
public class AppointmentController {

	@Autowired
	private AppointmentService appointmentService;

	@PostMapping
	public ResponseEntity<?> createAppointment(@RequestBody AppointmentRequest appointmentRequest) {
		appointmentService.makeAppointment(appointmentRequest.getDoctorId(), appointmentRequest.getPatientId(),
				appointmentRequest.getReason(), appointmentRequest.getAppointmentDate(),
				appointmentRequest.getServiceId());
		return ResponseEntity.ok().build();
	}

	@GetMapping("/{patientId}")
	public ResponseEntity<List<AppointmentRecord>> getAppointmentsByPatient(@PathVariable Long patientId) {
		return ResponseEntity.ok(appointmentService.getAppointmentsByPatientId(patientId));
	}

	@GetMapping("/doctor/{doctorId}")
	public ResponseEntity<List<AppointmentRecord>> getAppointmentsByDoctor(@PathVariable Long doctorId) {
		return ResponseEntity.ok(appointmentService.getAppointmentsByDoctorId(doctorId));
	}

	@PatchMapping("/{id}/status")
	@PreAuthorize("hasRole('DOCTOR')")
	public ResponseEntity<Void> updateStatus(@PathVariable Long id, @RequestBody StatusUpdateRequest request) {
		try {
			AppointmentStatus status = AppointmentStatus.valueOf(request.getStatus().toUpperCase());
			appointmentService.updateStatus(id, status);
			return ResponseEntity.noContent().build();
		} catch (IllegalArgumentException e) {
			return ResponseEntity.badRequest().build();
		}
	}

}
