package com.payment.payment.entity;

import java.math.BigDecimal;

import com.clinic.common.AuditablePersistableEntity;
import com.clinic.common.dto.DocumentType;
import com.clinic.common.dto.PaymentType;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "PAYMENT_DOCUMENT")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PaymentDocument extends AuditablePersistableEntity {
	private static final long serialVersionUID = 1L;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "OBLIGATION_ID", nullable = false)
	@JsonIgnore
	private PaymentObligation paymentObligation;

	@Enumerated(EnumType.STRING)
	@Column(name = "DOCUMENT_TYPE", nullable = false)
	private DocumentType documentType;

	@Enumerated(EnumType.STRING)
	@Column(name = "PAYMENT_TYPE")
	private PaymentType paymentType;

	@Column(name = "PAID_AMOUNT", nullable = false)
	private BigDecimal paidAmount;

	@Column(name = "SUCCESSFUL")
	private boolean successful;

}