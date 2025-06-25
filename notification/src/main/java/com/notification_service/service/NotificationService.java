package com.notification_service.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import com.clinic.common.dto.NotificationType;
import com.clinic.common.service.BaseService;
import com.notification_service.entity.Notification;
import com.notification_service.repository.NotificationRepository;

@Service
public class NotificationService extends BaseService<Notification> {

	@Autowired
	private NotificationRepository notificationRepository;

	@Autowired
	private SimpMessagingTemplate messagingTemplate;

	@Autowired
	private EmailService emailService;

	@Override
	protected JpaRepository<Notification, Long> getRepository() {
		return notificationRepository;
	}

	public void sendNotification(Long userId, String email, String message, NotificationType type) {
		Notification notif = new Notification();
		notif.setPatientId(userId);
		notif.setMessage(message);
		notif.setType(type);
		notif.setRead(false);
		messagingTemplate.convertAndSend("/topic/notifications/" + userId, notif);

		if (email != null && !email.isEmpty()) {
			String subject = "Notificare clinică";
			emailService.sendEmail(email, subject, message);
		}
		notificationRepository.save(notif);
	}

	public List<Notification> getPatientNotifications(Long patientId) {
		return notificationRepository.findByPatientId(patientId);
	}

	public void markAsRead(Long id) {
		Notification n = notificationRepository.findById(id).orElseThrow();
		n.setRead(true);
		notificationRepository.save(n);
	}

}
