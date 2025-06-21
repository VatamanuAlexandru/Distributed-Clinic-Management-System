package com.clinic.doctor.dto;

import java.time.DayOfWeek;
import java.time.LocalTime;

import com.clinic.mapper.BaseRecord;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class DoctorScheduleRecord extends BaseRecord {
	private static final long serialVersionUID = 1L;

	private DoctorRecord doctor;
	private DayOfWeek dayOfWeek;
	private LocalTime startTime;
	private LocalTime endTime;

}
