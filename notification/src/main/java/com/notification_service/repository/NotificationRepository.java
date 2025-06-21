package com.notification_service.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.notification_service.entity.Notification;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

	List<Notification> findByPatientId(Long patientId);
}
