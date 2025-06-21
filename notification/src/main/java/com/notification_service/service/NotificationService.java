package com.notification_service.service;

import java.util.List;

import org.jvnet.hk2.annotations.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;

import com.clinic.common.service.BaseService;
import com.notification_service.entity.Notification;
import com.notification_service.entity.NotificationType;
import com.notification_service.repository.NotificationRepository;

@Service
public class NotificationService extends BaseService<Notification> {

	@Autowired
	private NotificationRepository notificationRepository;

	@Override
	protected JpaRepository<Notification, Long> getRepository() {
		return notificationRepository;
	}

	public Notification sendNotification(Long patientId, String message, NotificationType type) {
		Notification notif = new Notification();
		notif.setPatientId(patientId);
		notif.setMessage(message);
		notif.setType(type);
		notif.setRead(false);
		return notificationRepository.save(notif);
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
