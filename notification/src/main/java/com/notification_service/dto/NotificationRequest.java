package com.notification_service.dto;

import com.notification_service.entity.NotificationType;

import lombok.Data;

@Data
public class NotificationRequest {
	private Long patientId;
	private String message;
	private NotificationType type;
}
