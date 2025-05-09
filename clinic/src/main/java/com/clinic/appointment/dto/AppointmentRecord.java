package com.clinic.appointment.dto;

import java.time.LocalDateTime;

import com.clinic.administrative.dto.OfficeRecord;
import com.clinic.appointment.entity.AppointmentStatus;
import com.clinic.doctor.dto.DoctorRecord;
import com.clinic.mapper.BaseRecord;
import com.clinic.patient.dto.PatientRecord;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class AppointmentRecord extends BaseRecord {
	private static final long serialVersionUID = 1L;

	private DoctorRecord doctor;
	private PatientRecord patient;
	private OfficeRecord office;
	private LocalDateTime appointmentDate;
	private AppointmentStatus status;
	private String reason;
	private LocalDateTime createdAt;

}
