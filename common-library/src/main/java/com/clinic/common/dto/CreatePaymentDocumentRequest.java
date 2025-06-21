package com.clinic.common.dto;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class CreatePaymentDocumentRequest {
	private Long obligationId;
	private DocumentType documentType;
	private PaymentType paymentType;
	private BigDecimal paidAmount;
	private boolean successful;
}
