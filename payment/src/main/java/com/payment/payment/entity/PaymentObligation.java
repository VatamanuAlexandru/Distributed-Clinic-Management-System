package com.payment.payment.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.clinic.common.AuditablePersistableEntity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "PAYMENT_OBLIGATION")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PaymentObligation extends AuditablePersistableEntity {
	private static final long serialVersionUID = 1L;

	@Column(name = "PATIENT_ID", nullable = false)
	private Long patientId;

	@Column(name = "AMOUNT", nullable = false)
	private BigDecimal amount;

	@Column(name = "DESCRIPTION", nullable = false)
	private String description;

	@Column(name = "DUE_DATE")
	private LocalDateTime dueDate;

	@Column(name = "PAID", nullable = false)
	private boolean paid;

	@OneToMany(mappedBy = "paymentObligation", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<PaymentDocument> documents = new ArrayList<>();

	public boolean isFullyPaid() {
		if (documents == null || documents.isEmpty())
			return false;
		BigDecimal totalPaid = documents.stream().filter(PaymentDocument::isSuccessful)
				.map(PaymentDocument::getPaidAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
		return totalPaid.compareTo(this.amount) >= 0;
	}

	public void markAsPaidIfApplicable() {
		if (isFullyPaid() && !this.paid) {
			this.paid = true;
		}
	}

}