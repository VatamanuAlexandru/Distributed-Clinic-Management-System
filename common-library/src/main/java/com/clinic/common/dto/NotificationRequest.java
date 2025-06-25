package com.clinic.common.dto;

import lombok.Data;

@Data
public class NotificationRequest {
	private Long userId; // sau patientId, cum preferi, dar generic e mai ok userId
	private String email; // Adresa de email a destinatarului
	private String message;
	private NotificationType type;
}
