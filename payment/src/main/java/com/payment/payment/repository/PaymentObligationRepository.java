package com.payment.payment.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.payment.payment.entity.PaymentObligation;

public interface PaymentObligationRepository extends JpaRepository<PaymentObligation, Long> {

	@Query("SELECT DISTINCT o FROM PaymentObligation o LEFT JOIN FETCH o.documents WHERE o.patientId = :patientId")
	List<PaymentObligation> findByPatientIdWithDocuments(@Param("patientId") Long patientId);

}
