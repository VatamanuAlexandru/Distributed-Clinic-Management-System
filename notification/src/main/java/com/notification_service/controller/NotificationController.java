package com.notification_service.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.clinic.common.dto.NotificationRequest;
import com.notification_service.entity.Notification;
import com.notification_service.service.NotificationService;

@RestController
@RequestMapping("/api/notification")
public class NotificationController {

	@Autowired
	private NotificationService notificationService;

	@PostMapping
	public ResponseEntity<Void> notify(@RequestBody NotificationRequest request) {
		notificationService.sendNotification(request.getUserId(), request.getEmail(), request.getMessage(),
				request.getType());
		return ResponseEntity.ok().build();
	}

	@GetMapping("/{patientId}")
	public ResponseEntity<List<Notification>> getAll(@PathVariable Long patientId) {
		return ResponseEntity.ok(notificationService.getPatientNotifications(patientId));
	}

	@PostMapping("/{id}/read")
	public ResponseEntity<Void> markAsRead(@PathVariable Long id) {
		notificationService.markAsRead(id);
		return ResponseEntity.ok().build();
	}
}
