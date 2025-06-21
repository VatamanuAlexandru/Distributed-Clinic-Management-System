package com.payment.payment.repository;

import java.math.BigDecimal;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.payment.payment.entity.PaymentDocument;

public interface PaymentDocumentRepository extends JpaRepository<PaymentDocument, Long> {

	@Query("SELECT SUM(d.paidAmount) FROM PaymentDocument d WHERE d.paymentObligation.id = :obligationId AND d.successful = true")
	BigDecimal sumPaidForObligation(@Param("obligationId") Long obligationId);

}
