package com.clinic.patient.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.clinic.mapper.BaseRecord;
import com.clinic.mapper.ExcludeMapping;
import com.clinic.patient.entity.TestCategory;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class MedicalTestRecord extends BaseRecord {
	private static final long serialVersionUID = 1L;

	@ExcludeMapping
	private MedicalHistoryRecord medicalHistory;
	private String testName;
	private TestCategory category;
	private String result;
	private LocalDateTime testDate;
	private List<TestParameterResultRecord> parameterResults;
}
