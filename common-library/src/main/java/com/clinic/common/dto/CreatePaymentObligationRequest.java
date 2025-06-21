package com.clinic.common.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.Data;

@Data
public class CreatePaymentObligationRequest {

	private Long patientId;
	private BigDecimal amount;
	private String description;
	private LocalDateTime dueDate;
}
