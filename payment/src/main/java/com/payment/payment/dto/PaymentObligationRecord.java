package com.payment.payment.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import com.clinic.mapper.BaseRecord;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class PaymentObligationRecord extends BaseRecord {
	private static final long serialVersionUID = 1L;
	
	private Long patientId;
	private BigDecimal amount;
	private String description;
	private LocalDateTime dueDate;
	private boolean paid;
	private List<PaymentDocumentRecord> documents;

}
