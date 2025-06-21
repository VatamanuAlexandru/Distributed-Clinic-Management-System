package com.notification_service.entity;

import com.clinic.common.AuditablePersistableEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "NOTIFICATION")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Notification extends AuditablePersistableEntity {
	private static final long serialVersionUID = 1L;

	@Column(name = "PATIENT_ID", nullable = false)
	private Long patientId;

	@Column(name = "MESSAGE", nullable = false)
	private String message;

	@Column(name = "READ", nullable = false)
	private boolean read = false;

	@Column(name = "TYPE")
	@Enumerated(EnumType.STRING)
	private NotificationType type;
}
