package com.clinic.patient.dto;

import java.time.LocalDateTime;

import com.clinic.mapper.BaseRecord;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class MedicalHistoryEntryRecord extends BaseRecord {
	private static final long serialVersionUID = 1L;

	private String type;
	private LocalDateTime date;
	private Object data;

}
