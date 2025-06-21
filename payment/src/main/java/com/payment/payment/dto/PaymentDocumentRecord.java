package com.payment.payment.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.clinic.common.dto.DocumentType;
import com.clinic.common.dto.PaymentType;
import com.clinic.mapper.BaseRecord;
import com.clinic.mapper.ExcludeMapping;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class PaymentDocumentRecord extends BaseRecord {
	private static final long serialVersionUID = 1L;

	@ExcludeMapping
	private PaymentObligationRecord paymentObligation;
	private DocumentType documentType;
	private PaymentType paymentType;
	private BigDecimal paidAmount;
	private boolean successful;
	private LocalDateTime createdAt;
}
